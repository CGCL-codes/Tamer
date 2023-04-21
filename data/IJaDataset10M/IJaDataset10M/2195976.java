package dk.i2m.converge.mobile.server.service;

import dk.i2m.converge.mobile.server.domain.Config;
import dk.i2m.converge.mobile.server.domain.NewsItem;
import dk.i2m.converge.mobile.server.domain.Outlet;
import dk.i2m.converge.mobile.server.facades.ConfigFacadeLocal;
import dk.i2m.converge.mobile.server.facades.SectionFacadeLocal;
import dk.i2m.converge.mobile.server.utils.BeanComparator;
import dk.i2m.converge.wsclient.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * RESTful service for initiating the synchronisation with <em>Converge
 * Editorial</em>. The service is initiated by passing:
 * <ul>
 * <li>ID of the external outlet ({@code eid})</li>
 * <li>ID of the edition to download ({@code edition_id})</li>
 * <li>ID of the internal outlet ({@code internal_outlet_id})</li>
 * <li>Key of the internal outlet ({@code internal_outlet_key})</li>
 * </li>
 * <p/>
 * The parameters are passed in the URL path as followed:
 * {@code /wakeup/[eid]/[edition_id]/[internal_outlet_id]/[internal_outlet_key]}
 *
 * @author Allan Lykke Christensen
 */
@Stateless
@Path("/wakeup/{eid}/{edition}/{iid}/{key}")
public class WakeupService {

    @EJB
    private ConfigFacadeLocal cfgFacade;

    @EJB
    private SectionFacadeLocal sectionFacade;

    @Context
    ServletContext context;

    @PersistenceContext(unitName = "cmsPU")
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(WakeupService.class.getName());

    private static final String SERVICE_URI = "http://soap.ws.converge.i2m.dk/";

    private static final String OUTLET_SERVICE_NAME = "OutletService";

    /**
     * Initiates the download of a given edition on <em>Converge Editorial</em>.
     *
     * @param externalId  Unique identifier of the Outlet on <em>Converge Editorial</em>
     * @param internalId  Internal identifier of the Outlet on <em>Converge Mobile Server</em>
     * @param internalKey Internal key of the Outlet on <em>Converge Mobile Server</em>
     * @param editionId   Unique identifier of the Edition to download on <em>Converge Editorial</em>
     * @return Empty string
     */
    @GET
    public String handleGET(@PathParam(value = "eid") String externalId, @PathParam(value = "iid") String internalId, @PathParam(value = "key") String internalKey, @PathParam(value = "edition") String editionId) {
        try {
            LOG.log(Level.INFO, "Synchronisation of External Outlet {0} with Internal Outlet {1} with key {2}", new Object[] { externalId, internalId, internalKey });
            Outlet outlet = em.find(Outlet.class, Long.valueOf(internalId));
            if (!outlet.getKey().equals(internalKey)) {
                LOG.log(Level.WARNING, "Invalid key provided by invoker {0}, expecting {1}", new Object[] { internalKey, outlet.getKey() });
                return "";
            }
            Long externalOutletId = Long.valueOf(outlet.getExternalId());
            Long externalEditionId = Long.valueOf(editionId);
            Authenticator.setDefault(new MyAuthenticator(outlet.getExternalUid(), outlet.getExternalPwd()));
            URL url = new URL(outlet.getExternalUrl());
            QName qname = new QName(SERVICE_URI, OUTLET_SERVICE_NAME);
            Service service = OutletService_Service.create(url, qname);
            OutletService os = service.getPort(OutletService.class);
            dk.i2m.converge.wsclient.Outlet externalOutlet = os.getOutlet(externalOutletId);
            for (Section s : externalOutlet.getSections()) {
                if (isNewSection(s.getId())) {
                    dk.i2m.converge.mobile.server.domain.Section section = new dk.i2m.converge.mobile.server.domain.Section();
                    section.setTitle(s.getTitle());
                    section.setExternalId(s.getId());
                    section.setDisplayOrder(1);
                    em.persist(section);
                    outlet.getSections().add(section);
                    em.merge(outlet);
                }
            }
            dk.i2m.converge.wsclient.Edition externalEdition = os.getPublishedEdition(externalEditionId);
            em.createQuery("UPDATE NewsItem ni " + "SET ni.available = ?1").setParameter(1, false).executeUpdate();
            Config rndThumb = cfgFacade.find(Config.Property.RENDITION_THUMB);
            Config rndStory = cfgFacade.find(Config.Property.RENDITION_STORY);
            Config imgLocation = cfgFacade.find(Config.Property.IMAGE_LOCATION);
            Config imgUrl = cfgFacade.find(Config.Property.IMAGE_URL);
            Config imgTimeout = cfgFacade.find(Config.Property.IMAGE_DOWNLOAD_TIMEOUT);
            int timeout = Integer.valueOf(imgTimeout.getValue());
            for (dk.i2m.converge.wsclient.NewsItem item : externalEdition.getItems()) {
                if (!isNewsItemAvailable(item.getId())) {
                    NewsItem newsItem = new NewsItem();
                    newsItem.setExternalId(item.getId());
                    newsItem.setHeadline(item.getTitle());
                    newsItem.setStory(item.getStory());
                    newsItem.setAvailable(true);
                    newsItem.setDateline(item.getDateLine());
                    newsItem.setByline(item.getByLine());
                    newsItem.setDisplayOrder(item.getDisplayOrder());
                    if (!item.getMedia().isEmpty()) {
                        Collections.sort(item.getMedia(), new BeanComparator("priority"));
                        MediaItem img = item.getMedia().iterator().next();
                        for (MediaItemRendition mir : img.getRenditions()) {
                            String postfix = "";
                            boolean isThumbImg = false;
                            boolean isStoryImg = false;
                            if (mir.getName().equalsIgnoreCase(rndThumb.getValue())) {
                                isThumbImg = true;
                                postfix = "-thumb";
                            } else if (mir.getName().equalsIgnoreCase(rndStory.getValue())) {
                                isStoryImg = true;
                                postfix = "-story";
                            } else {
                                continue;
                            }
                            try {
                                String filename = "" + img.getId() + postfix;
                                URL mirImg = new URL(mir.getUrl());
                                File copyTo = new File(imgLocation.getValue(), filename);
                                LOG.log(Level.INFO, "Downloading {0} to {1}", new Object[] { mirImg.toExternalForm(), copyTo.getCanonicalPath() });
                                org.apache.commons.io.FileUtils.copyURLToFile(mirImg, copyTo, timeout, timeout);
                                if (isThumbImg) {
                                    newsItem.setThumbUrl(imgUrl.getValue() + "/" + filename);
                                } else if (isStoryImg) {
                                    newsItem.setImgUrl(imgUrl.getValue() + "/" + filename);
                                }
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        String sid = String.valueOf(item.getSection().getId());
                        dk.i2m.converge.mobile.server.domain.Section s = sectionFacade.findByExternalId(item.getSection().getId());
                        if (s.isDefaultStoryImageAvailable()) {
                            newsItem.setImgUrl(s.getDefaultStoryImageUrl());
                        }
                        if (s.isDefaultStoryThumbImageAvailable()) {
                            newsItem.setThumbUrl(s.getDefaultStoryThumbImageUrl());
                        }
                    }
                    newsItem.setSection(findSectionByExternalId(item.getSection().getId()));
                    em.persist(newsItem);
                } else {
                    em.createQuery("UPDATE NewsItem ni SET ni.available = ?1 WHERE ni.externalId = ?2").setParameter(1, true).setParameter(2, item.getId()).executeUpdate();
                }
            }
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private boolean isUrlAvailable(String url) {
        try {
            URL test = new URL(url);
            test.openConnection();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(WakeupService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    static class MyAuthenticator extends Authenticator {

        private String username = "";

        private String password = "";

        public MyAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(this.username, this.password.toCharArray()));
        }
    }

    private boolean isNewSection(Long section) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root subs = cq.from(dk.i2m.converge.mobile.server.domain.Section.class);
        cq.select(subs).where(cb.equal(subs.get("externalId"), section));
        List<dk.i2m.converge.mobile.server.domain.Section> matches = em.createQuery(cq).getResultList();
        return matches.isEmpty();
    }

    private dk.i2m.converge.mobile.server.domain.Section findSectionByExternalId(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root subs = cq.from(dk.i2m.converge.mobile.server.domain.Section.class);
        cq.select(subs).where(cb.equal(subs.get("externalId"), id));
        List<dk.i2m.converge.mobile.server.domain.Section> matches = em.createQuery(cq).getResultList();
        if (matches.isEmpty()) {
            return null;
        } else {
            return matches.iterator().next();
        }
    }

    private dk.i2m.converge.mobile.server.domain.NewsItem findNewsItemByExternalId(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root subs = cq.from(dk.i2m.converge.mobile.server.domain.NewsItem.class);
        cq.select(subs).where(cb.equal(subs.get("externalId"), id));
        List<dk.i2m.converge.mobile.server.domain.NewsItem> matches = em.createQuery(cq).getResultList();
        if (matches.isEmpty()) {
            return null;
        } else {
            return matches.iterator().next();
        }
    }

    private boolean isNewsItemAvailable(Long externalId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root subs = cq.from(dk.i2m.converge.mobile.server.domain.NewsItem.class);
        cq.select(subs).where(cb.equal(subs.get("externalId"), externalId));
        List<dk.i2m.converge.mobile.server.domain.NewsItem> matches = em.createQuery(cq).getResultList();
        return !matches.isEmpty();
    }

    private byte[] getBytesFromUrl(URL url) {
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = url.openStream();
            byte[] byteChunk = new byte[4096];
            int n;
            while ((n = is.read(byteChunk)) > 0) {
                bais.write(byteChunk, 0, n);
            }
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return bais.toByteArray();
    }

    public byte[] convertJpgToPng(String inputFile, String outputFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(inputFile));
            ImageIO.write(bufferedImage, "png", new File(outputFile));
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOut);
            return byteArrayOut.toByteArray();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }
}
