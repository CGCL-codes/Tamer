package fr.soleil.mambo.datasources.tango.standard;

import java.util.Vector;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tango.utils.DevFailedUtils;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.ApiUtil;
import fr.esrf.TangoApi.DeviceProxy;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.datasources.db.DbConnectionManager;
import fr.soleil.tango.util.entity.data.Attribute;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;

public class BasicTangoManagerImpl implements ITangoManager {

    static final Logger logger = LoggerFactory.getLogger(BasicTangoManagerImpl.class);

    public BasicTangoManagerImpl() {
    }

    @Override
    public Vector<Domain> loadDomains(final String searchCriterions, final boolean forceReload) {
        final Vector<Domain> ret = new Vector<Domain>();
        String devicePattern = searchCriterions;
        if (searchCriterions.contains("/")) {
            devicePattern = searchCriterions.substring(0, searchCriterions.lastIndexOf('/'));
        }
        String attributePattern = GUIUtilities.TANGO_JOKER;
        if (searchCriterions.contains("/")) {
            attributePattern = searchCriterions.substring(searchCriterions.lastIndexOf('/') + 1);
            if (!attributePattern.equals(GUIUtilities.TANGO_JOKER)) {
                logger.warn("looking for attribute {} can be very slow", attributePattern);
            }
        }
        logger.debug("looking for devices with pattern: device name=  {}, attribute name = {}", devicePattern, attributePattern);
        try {
            final String[] deviceNames = ApiUtil.get_db_obj().get_device_exported(devicePattern);
            logger.debug("found  {} devices for pattern {}", deviceNames.length, devicePattern);
            int attributesFound = 0;
            for (final String deviceName : deviceNames) {
                final String[] split = deviceName.split("/");
                if (split.length == 3) {
                    final String domain = split[0];
                    final String family = split[1];
                    final String member = split[2];
                    String[] attributeNames = new String[0];
                    if (!attributePattern.equals(GUIUtilities.TANGO_JOKER)) {
                        try {
                            final DeviceProxy deviceProxy = new DeviceProxy(deviceName);
                            attributeNames = deviceProxy.get_attribute_list();
                        } catch (final DevFailed e) {
                        }
                        String attrPattern = attributePattern;
                        attrPattern = attrPattern.replace("*", ".*");
                        attrPattern = attrPattern.replace("?", ".");
                        for (final String currentAttributeName : attributeNames) {
                            if (Pattern.matches(attrPattern, currentAttributeName)) {
                                attributesFound++;
                                final Attribute currenteAttribute = new Attribute(currentAttributeName);
                                int i = 0;
                                for (final Domain domaine : ret) {
                                    if (domaine.getName().equalsIgnoreCase(domain)) {
                                        if (domaine.getFamilies().containsKey(family)) {
                                            if (domaine.getFamilies().get(family).getMembers().containsKey(member)) {
                                                domaine.getFamilies().get(family).getMembers().get(member).addAttribute(currenteAttribute);
                                                break;
                                            } else {
                                                final Member m = new Member(member);
                                                m.addAttribute(currenteAttribute);
                                                domaine.getFamilies().get(family).addMember(m);
                                                break;
                                            }
                                        } else {
                                            final Family f = new Family(family);
                                            final Member m = new Member(member);
                                            m.addAttribute(currenteAttribute);
                                            f.addMember(m);
                                            domaine.addFamily(f);
                                            break;
                                        }
                                    }
                                    i++;
                                }
                                if (i == ret.size()) {
                                    final Domain d = new Domain(domain);
                                    final Family f = new Family(family);
                                    final Member m = new Member(member);
                                    m.addAttribute(currenteAttribute);
                                    f.addMember(m);
                                    d.addFamily(f);
                                    ret.add(d);
                                }
                            }
                        }
                    } else {
                        int i = 0;
                        for (final Domain domaine : ret) {
                            if (domaine.getName().equalsIgnoreCase(domain)) {
                                if (domaine.getFamilies().containsKey(family)) {
                                    if (domaine.getFamilies().get(family).getMembers().containsKey(member)) {
                                        break;
                                    } else {
                                        final Member m = new Member(member);
                                        domaine.getFamilies().get(family).addMember(m);
                                        break;
                                    }
                                } else {
                                    final Family f = new Family(family);
                                    final Member m = new Member(member);
                                    f.addMember(m);
                                    domaine.addFamily(f);
                                    break;
                                }
                            }
                            i++;
                        }
                        if (i == ret.size()) {
                            final Domain d = new Domain(domain);
                            final Family f = new Family(family);
                            final Member m = new Member(member);
                            f.addMember(m);
                            d.addFamily(f);
                            ret.add(d);
                        }
                    }
                }
                if (!attributePattern.equals(GUIUtilities.TANGO_JOKER)) {
                    logger.debug("found {} attribute(s) on {} device(s)", attributesFound, deviceNames.length);
                }
            }
        } catch (final DevFailed e) {
            logger.error("tango error {}", DevFailedUtils.toString(e));
        }
        return ret;
    }

    @Override
    public Archiver[] findArchivers(final boolean historic) throws ArchivingException {
        final String[] list = DbConnectionManager.getArchivingManagerApiInstance(historic).getM_ExportedArchiverList();
        if (list == null || list.length == 0) {
            return null;
        }
        final int numberOfArchivers = list.length;
        final Archiver[] ret = new Archiver[numberOfArchivers];
        for (int i = 0; i < numberOfArchivers; i++) {
            final String nextName = list[i];
            ret[i] = new Archiver(nextName, historic);
        }
        return ret;
    }
}
