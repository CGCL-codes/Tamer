package yarfraw.utils.extension;

import static yarfraw.io.parser.ExtensionElementQname.*;
import static yarfraw.utils.XMLUtils.same;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.georss.georss._10.GeoRssExtension;
import org.georss.georss._10.SimplePositionType;
import org.georss.georss._10.Where;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import yarfraw.core.datamodel.FeedFormat;
import yarfraw.core.datamodel.YarfrawException;
import yarfraw.generated.admin.elements.AdminExtension;
import yarfraw.generated.admin.elements.AdminType;
import yarfraw.generated.atom03.ext.elements.Atom03Extension;
import yarfraw.generated.atom03.ext.elements.ContentType;
import yarfraw.generated.blogger.elements.BloggerExtension;
import yarfraw.generated.feedburner.elements.FeedburnerExtension;
import yarfraw.generated.googlebase.elements.CurrencyCodeEnumeration;
import yarfraw.generated.googlebase.elements.DateTimeRangeType;
import yarfraw.generated.googlebase.elements.GenderEnumeration;
import yarfraw.generated.googlebase.elements.GoogleBaseExtension;
import yarfraw.generated.googlebase.elements.ObjectFactory;
import yarfraw.generated.googlebase.elements.PaymentMethodEnumeration;
import yarfraw.generated.googlebase.elements.PriceTypeEnumeration;
import yarfraw.generated.googlebase.elements.ShippingType;
import yarfraw.generated.itunes.elements.ItunesCategoryType;
import yarfraw.generated.itunes.elements.ItunesExtension;
import yarfraw.generated.itunes.elements.ItunesImageType;
import yarfraw.generated.itunes.elements.ItunesOwnerType;
import yarfraw.generated.mrss.elements.MrssCategoryType;
import yarfraw.generated.mrss.elements.MrssContentType;
import yarfraw.generated.mrss.elements.MrssCopyrightType;
import yarfraw.generated.mrss.elements.MrssCreditType;
import yarfraw.generated.mrss.elements.MrssDescriptionType;
import yarfraw.generated.mrss.elements.MrssExtension;
import yarfraw.generated.mrss.elements.MrssGroupType;
import yarfraw.generated.mrss.elements.MrssHashType;
import yarfraw.generated.mrss.elements.MrssPlayerType;
import yarfraw.generated.mrss.elements.MrssRatingType;
import yarfraw.generated.mrss.elements.MrssRestrictionType;
import yarfraw.generated.mrss.elements.MrssTextType;
import yarfraw.generated.mrss.elements.MrssThumbnailType;
import yarfraw.generated.mrss.elements.MrssTitleType;
import yarfraw.generated.rss10.elements.DcType;
import yarfraw.generated.rss10.elements.DublinCoreExtension;
import yarfraw.generated.rss10.elements.SyndicationExtension;
import yarfraw.generated.rss10.elements.UpdatePeriodEnum;
import yarfraw.generated.slash.elements.SlashExtension;
import yarfraw.generated.wfw.elements.WellFormedWebExtension;
import yarfraw.utils.JAXBUtils;

public class ExtensionUtils {

    private static final String HTTP_BASE_GOOGLE_COM_CNS_1_0 = "http://base.google.com/ns/1.0";

    private static final String ITUNES_JAXB_CONTEXT = "yarfraw.generated.itunes.elements";

    private static final String MRSS_JAXB_CONTEXT = "yarfraw.generated.mrss.elements";

    private static final String GOOGLEBASE_JAXB_CONTEXT = "yarfraw.generated.googlebase.elements";

    private static final String DUBLINCORE_JAXB_CONTEXT = "yarfraw.generated.rss10.elements";

    private static final String GEORSS_JAXB_CONTEXT = "org.georss.georss._10";

    private static final String WFW_JAXB_CONTEXT = "yarfraw.generated.wfw.elements";

    private static final String SY_JAXB_CONTEXT = "yarfraw.generated.rss10.elements";

    private static final String BLOGGER_JAXB_CONTEXT = "yarfraw.generated.blogger.elements";

    private static final String ADMIN_JAXB_CONTEXT = "yarfraw.generated.admin.elements";

    private static final String FEEDBURNER_JAXB_CONTEXT = "yarfraw.generated.feedburner.elements";

    private static final String SLASH_JAXB_CONTEXT = "yarfraw.generated.slash.elements";

    private static final String ATOM03_JAXB_CONTEXT = "yarfraw.generated.atom03.ext.elements";

    private static enum ContextEnum {

        DC, SY, ITUNES, GOOGLEBASE, WFW, MRSS, GEORSS, BLOGGER, ADMIN, FEEDBURNER, SLASH, ATOM03
    }

    private static final ObjectFactory GOOGLEBASE_FACTORY = new ObjectFactory();

    private static final Log LOG = LogFactory.getLog(ExtensionUtils.class);

    private ExtensionUtils() {
    }

    /**
   * Extracts the Atom 0.3 extension elements <b>that are not mapped to the core models</b>
   * from the input list into an {@link Atom03Extension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://yarfraw.sourceforge.net about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link Atom03Extension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static Atom03Extension extractAtom03Extension(List<Element> otherElements) throws YarfrawException {
        Atom03Extension ret = new Atom03Extension();
        try {
            if (otherElements != null) {
                Unmarshaller u = getContext(ContextEnum.ATOM03).createUnmarshaller();
                Iterator<Element> it = otherElements.iterator();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, ATOM03_CONTENT_QNAME)) {
                        ContentType c = ((JAXBElement<ContentType>) u.unmarshal(e)).getValue();
                        ret.getContent().add(c);
                        it.remove();
                    } else if (same(name, ATOM03_INFO_QNAME)) {
                        ContentType c = ((JAXBElement<ContentType>) u.unmarshal(e)).getValue();
                        ret.setInfo(c);
                        it.remove();
                    } else if (same(name, ATOM03_ISSUED_QNAME)) {
                        ret.setIssued(e.getTextContent());
                        it.remove();
                    } else if (same(name, ATOM03_SUMMARY_QNAME)) {
                        ContentType c = ((JAXBElement<ContentType>) u.unmarshal(e)).getValue();
                        ret.setSummary(c);
                        it.remove();
                    } else if (same(name, ATOM03_TITLE_QNAME)) {
                        ContentType c = ((JAXBElement<ContentType>) u.unmarshal(e)).getValue();
                        ret.setTitle(c);
                        it.remove();
                    } else if (same(name, ATOM03_TAGLINE_QNAME)) {
                        ContentType c = ((JAXBElement<ContentType>) u.unmarshal(e)).getValue();
                        ret.setTagline(c);
                        it.remove();
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the itunes extension elements from the input list into an {@link ItunesExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://www.apple.com/itunes/store/podcaststechspecs.html about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link ItunesExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static ItunesExtension extractItunesExtension(List<Element> otherElements) throws YarfrawException {
        ItunesExtension ret = new ItunesExtension();
        try {
            if (otherElements != null) {
                Unmarshaller u = getContext(ContextEnum.ITUNES).createUnmarshaller();
                Iterator<Element> it = otherElements.iterator();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, ITUNES_AUTHOR_QNAME)) {
                        ret.setAuthor(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_BLOCK_QNAME)) {
                        ret.setBlock(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_CATEGORY_QNAME)) {
                        ItunesCategoryType cat = ((JAXBElement<ItunesCategoryType>) u.unmarshal(e)).getValue();
                        ret.getCategory().add(cat);
                        it.remove();
                    } else if (same(name, ITUNES_DURATION_QNAME)) {
                        ret.setDuration(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_EXPLICIT_QNAME)) {
                        ret.setExplicit(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_IMAGE_QNAME)) {
                        ItunesImageType img = ((JAXBElement<ItunesImageType>) u.unmarshal(e)).getValue();
                        ret.setImage(img);
                        it.remove();
                    } else if (same(name, ITUNES_KEYWORDS_QNAME)) {
                        ret.setKeywords(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_OWNER_QNAME)) {
                        ItunesOwnerType owner = ((JAXBElement<ItunesOwnerType>) u.unmarshal(e)).getValue();
                        ret.setOwner(owner);
                        it.remove();
                    } else if (same(name, ITUNES_SUBTITLE_QNAME)) {
                        ret.setSubtitle(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_SUMMARY_QNAME)) {
                        ret.setSummary(e.getTextContent());
                        it.remove();
                    } else if (same(name, ITUNES_NewFeedUrl_QNAME)) {
                        ret.setNewFeedUrl(e.getTextContent());
                        it.remove();
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the itunes extension elements from the input list into an {@link DublinCoreExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://dublincore.org/documents/2002/07/31/dcmes-xml/ about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link DublinCoreExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static DublinCoreExtension extractDublinCoreExtension(List<Element> otherElements) throws YarfrawException {
        DublinCoreExtension ret = new DublinCoreExtension();
        try {
            if (otherElements != null) {
                Iterator<Element> it = otherElements.iterator();
                Unmarshaller u = getContext(ContextEnum.SY).createUnmarshaller();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, DC_Contributor_QNAME)) {
                        ret.getContributor().add(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Coverage_QNAME)) {
                        ret.setCoverage(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Creator_QNAME)) {
                        ret.setCreator(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Date_QNAME)) {
                        ret.setDate(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Description_QNAME)) {
                        ret.setDescription(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Format_QNAME)) {
                        ret.setFormat(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Identifier_QNAME)) {
                        ret.setIdentifier(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Language_QNAME)) {
                        ret.setLanguage(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Publisher_QNAME)) {
                        ret.setPublisher(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Relation_QNAME)) {
                        ret.setRelation(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Rights_QNAME)) {
                        ret.setRights(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Source_QNAME)) {
                        ret.setSource(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Subject_QNAME)) {
                        ret.getSubject().add(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Title_QNAME)) {
                        ret.setTitle(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, DC_Type_QNAME)) {
                        ret.setType(((JAXBElement<DcType>) u.unmarshal(e)).getValue());
                        it.remove();
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the well-formed web extension elements from the input list into an {@link WellFormedWebExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://wellformedweb.org/news/wfw_namespace_elements about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link WellFormedWebExtension} object
   * @throws YarfrawException 
   */
    public static WellFormedWebExtension extractWellFormedWebExtension(List<Element> otherElements) throws YarfrawException {
        WellFormedWebExtension ret = new WellFormedWebExtension();
        if (otherElements != null) {
            Iterator<Element> it = otherElements.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                if (e == null) {
                    continue;
                }
                QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                if (same(name, WFW_Comment_QNAME)) {
                    ret.setComment(e.getTextContent());
                    it.remove();
                } else if (same(name, WFW_CommentRss_QNAME)) {
                    ret.setCommentRss(e.getTextContent());
                    it.remove();
                }
            }
        }
        return ret;
    }

    /**
   * Extracts the well-formed web extension elements from the input list into an {@link AdminExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/admin/ about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link AdminExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static AdminExtension extractAdminExtension(List<Element> otherElements) throws YarfrawException {
        AdminExtension ret = new AdminExtension();
        try {
            if (otherElements != null) {
                Iterator<Element> it = otherElements.iterator();
                Unmarshaller u = getContext(ContextEnum.ADMIN).createUnmarshaller();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, ADMIN_ErrorReportsTo_QNAME)) {
                        if (e.getTextContent() != null) {
                            ret.setErrorReportsTo(((JAXBElement<AdminType>) u.unmarshal(e)).getValue());
                        }
                    } else if (same(name, ADMIN_GeneratorAgent_QNAME)) {
                        if (e.getTextContent() != null) {
                            ret.setGeneratorAgent(((JAXBElement<AdminType>) u.unmarshal(e)).getValue());
                        }
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the feedburner extension elements from the input list into an {@link FeedburnerExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see www.feedburner.com about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link FeedburnerExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static FeedburnerExtension extractFeedburnerExtension(List<Element> otherElements) throws YarfrawException {
        FeedburnerExtension ret = new FeedburnerExtension();
        if (otherElements != null) {
            Iterator<Element> it = otherElements.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                if (e == null) {
                    continue;
                }
                QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                if (same(name, FEEDBURNER_BrowserFriendly_QNAME)) {
                    ret.setBrowserFriendly(e.getTextContent());
                } else if (same(name, FEEDBURNER_OrigLink_QNAME)) {
                    ret.setOrigLink(e.getTextContent());
                }
            }
        }
        return ret;
    }

    /**
   * Extracts the slash extension elements from the input list into an {@link SlashExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/slash/ these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link SlashExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static SlashExtension extractSlashExtension(List<Element> otherElements) throws YarfrawException {
        SlashExtension ret = new SlashExtension();
        try {
            if (otherElements != null) {
                Iterator<Element> it = otherElements.iterator();
                Unmarshaller u = getContext(ContextEnum.SLASH).createUnmarshaller();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, SLASH_Comments_QNAME)) {
                        ret.setComments(((JAXBElement<BigInteger>) u.unmarshal(e)).getValue());
                    } else if (same(name, SLASH_Department_QNAME)) {
                        ret.setDepartment(e.getTextContent());
                    } else if (same(name, SLASH_HitParade_QNAME)) {
                        ret.setHitParade(e.getTextContent());
                    } else if (same(name, SLASH_Section_QNAME)) {
                        ret.setSection(e.getTextContent());
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the well-formed web extension elements from the input list into an {@link BloggerExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://code.blogger.com/archives/atom-docs.html#extensions about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link BloggerExtension} object
   * @throws YarfrawException 
   */
    public static BloggerExtension extractBloggerExtension(List<Element> otherElements) throws YarfrawException {
        BloggerExtension ret = new BloggerExtension();
        if (otherElements != null) {
            Iterator<Element> it = otherElements.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                if (e == null) {
                    continue;
                }
                QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                if (same(name, BLOGGER_ConvertLineBreaks_QNAME)) {
                    if (e.getTextContent() != null) {
                        ret.setConvertLineBreaks(Boolean.valueOf(e.getTextContent()));
                    }
                } else if (same(name, BLOGGER_Draft_QNAME)) {
                    if (e.getTextContent() != null) {
                        ret.setDraft(Boolean.valueOf(e.getTextContent()));
                    }
                }
            }
        }
        return ret;
    }

    /**
   * Extracts the well-formed web extension elements from the input list into an {@link SyndicationExtension}
   * object. <br/>
   * The extracted elements will be removed from the original input list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/syndication/ about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link SyndicationExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static SyndicationExtension extractSyndicationExtension(List<Element> otherElements) throws YarfrawException {
        SyndicationExtension ret = new SyndicationExtension();
        try {
            if (otherElements != null) {
                Iterator<Element> it = otherElements.iterator();
                Unmarshaller u = getContext(ContextEnum.SY).createUnmarshaller();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, SY_UpdateBase_QNAME)) {
                        ret.setUpdateBase(e.getTextContent());
                        it.remove();
                    } else if (same(name, SY_UpdateFrequency_QNAME)) {
                        ret.setUpdateFrequency(((JAXBElement<BigInteger>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, SY_UpdatePeriod_QNAME)) {
                        ret.setUpdatePeriod(((JAXBElement<UpdatePeriodEnum>) u.unmarshal(e)).getValue());
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the googlebase extension elements from the input list into an {@link GoogleBaseExtension}
   * object.
   * <br/>
   * see http://www.apple.com/itunes/store/podcaststechspecs.html about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link GoogleBaseExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static GoogleBaseExtension extractGoogleBaseExtension(List<Element> otherElements) throws YarfrawException {
        GoogleBaseExtension ret = new GoogleBaseExtension();
        try {
            if (otherElements != null) {
                Unmarshaller u = JAXBContext.newInstance(GOOGLEBASE_JAXB_CONTEXT).createUnmarshaller();
                Iterator<Element> it = otherElements.iterator();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, GOOGLEBASE_Actor_QNAME)) {
                        ret.getActor().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Agent_QNAME)) {
                        ret.getAgent().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Age_QNAME)) {
                        ret.setAge(e.getTextContent() == null ? null : Short.valueOf(e.getTextContent()));
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ApparelType_QNAME)) {
                        ret.setApparelType(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Area_QNAME)) {
                        ret.setArea(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Artist_QNAME)) {
                        ret.getArtist().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Author_QNAME)) {
                        ret.getAuthor().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Bathrooms_QNAME)) {
                        ret.setBathrooms(e.getTextContent() == null ? null : new BigDecimal(e.getTextContent()));
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Bedrooms_QNAME)) {
                        ret.setBedrooms(e.getTextContent() == null ? null : Short.valueOf(e.getTextContent()));
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Brand_QNAME)) {
                        ret.setBrand(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Color_QNAME)) {
                        ret.getColor().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Condition_QNAME)) {
                        ret.setCondition(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_CourseDateRange_QNAME)) {
                        DateTimeRangeType d = ((JAXBElement<DateTimeRangeType>) u.unmarshal(e)).getValue();
                        ret.setCourseDateRange(d);
                        it.remove();
                    } else if (same(name, GOOGLEBASE_CourseNumber_QNAME)) {
                        ret.setCourseNumber(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_CourseTimes_QNAME)) {
                        ret.setCourseTimes(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Currency_QNAME)) {
                        ret.setCurrency(((JAXBElement<CurrencyCodeEnumeration>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_DeliveryNotes_QNAME)) {
                        ret.setDeliveryNotes(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_DeliveryRadius_QNAME)) {
                        ret.setDeliveryRadius(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_DevelopmentStatus_QNAME)) {
                        ret.setDevelopmentStatus(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Education_QNAME)) {
                        ret.getEducation().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Employer_QNAME)) {
                        ret.getEmployer().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Ethnicity_QNAME)) {
                        ret.getEthnicity().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_EventDateRange_QNAME)) {
                        DateTimeRangeType d = ((JAXBElement<DateTimeRangeType>) u.unmarshal(e)).getValue();
                        ret.setEventDateRange(d);
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ExpirationDate_QNAME)) {
                        XMLGregorianCalendar d = ((JAXBElement<XMLGregorianCalendar>) u.unmarshal(e)).getValue();
                        ret.setExpirationDate(d);
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ExpirationDateTime_QNAME)) {
                        XMLGregorianCalendar d = ((JAXBElement<XMLGregorianCalendar>) u.unmarshal(e)).getValue();
                        ret.setExpirationDateTime(d);
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Format_QNAME)) {
                        ret.getFormat().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ForSale_QNAME)) {
                        ret.setForSale(((JAXBElement<Boolean>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_FromLocation_QNAME)) {
                        ret.setFromLocation(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Gender_QNAME)) {
                        ret.setGender(((JAXBElement<GenderEnumeration>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_HoaDues_QNAME)) {
                        ret.setHoaDues(((JAXBElement<BigDecimal>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Id_QNAME)) {
                        ret.setId(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ImageLink_QNAME)) {
                        ret.getImageLink().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ImmigrationStatus_QNAME)) {
                        ret.setImmigrationStatus(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_InterestedIn_QNAME)) {
                        ret.setInterestedIn(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Isbn_QNAME)) {
                        ret.setIsbn(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_JobFunction_QNAME)) {
                        ret.getJobFunction().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_JobIndustry_QNAME)) {
                        ret.getJobIndustry().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_JobType_QNAME)) {
                        ret.getJobType().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Label_QNAME)) {
                        ret.getLabel().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_License_QNAME)) {
                        ret.getLicense().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ListingType_QNAME)) {
                        ret.getListingType().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Location_QNAME)) {
                        ret.setLocation(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Make_QNAME)) {
                        ret.setMake(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Manufacturer_QNAME)) {
                        ret.setManufacturer(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ManufacturerId_QNAME)) {
                        ret.setManufacturerId(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_MaritalStatus_QNAME)) {
                        ret.setMaritalStatus(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Megapixels_QNAME)) {
                        ret.setMegapixels(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Memory_QNAME)) {
                        ret.setMemory(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Mileage_QNAME)) {
                        ret.setMileage(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Model_QNAME)) {
                        ret.setModel(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ModelNumber_QNAME)) {
                        ret.setModelNumber(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_NameOfItemReviewed_QNAME)) {
                        ret.setNameOfItemReviewed(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_NewsSource_QNAME)) {
                        ret.setNewsSource(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Occupation_QNAME)) {
                        ret.setOccupation(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_OperatingSystems_QNAME)) {
                        ret.setOperatingSystems(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Pages_QNAME)) {
                        ret.setPages(((JAXBElement<BigInteger>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PaymentAccepted_QNAME)) {
                        ret.getPaymentAccepted().add(((JAXBElement<PaymentMethodEnumeration>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PaymentNotes_QNAME)) {
                        ret.setPaymentNotes(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Pickup_QNAME)) {
                        ret.setPickup(((JAXBElement<Boolean>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Price_QNAME)) {
                        ret.setPrice(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PriceType_QNAME)) {
                        ret.setPriceType(((JAXBElement<PriceTypeEnumeration>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ProcessorSpeed_QNAME)) {
                        ret.setProcessorSpeed(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ProductType_QNAME)) {
                        ret.getProductType().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ProgrammingLanguage_QNAME)) {
                        ret.getProgrammingLanguage().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PropertyType_QNAME)) {
                        ret.getPropertyType().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PublicationName_QNAME)) {
                        ret.setPublicationName(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PublicationVolume_QNAME)) {
                        ret.setPublicationVolume(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_PublishDate_QNAME)) {
                        ret.setPublishDate(((JAXBElement<XMLGregorianCalendar>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Quantity_QNAME)) {
                        ret.setQuantity(((JAXBElement<BigInteger>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Rating_QNAME)) {
                        ret.setRating(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_RatingType_QNAME)) {
                        ret.setRatingType(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_RelatedLink_QNAME)) {
                        ret.getRelatedLink().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ReviewerType_QNAME)) {
                        ret.setReviewerType(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ReviewType_QNAME)) {
                        ret.setReviewType(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Salary_QNAME)) {
                        ret.setSalary(((JAXBElement<BigDecimal>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_SalaryType_QNAME)) {
                        ret.setSalaryType(((JAXBElement<PriceTypeEnumeration>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_SchoolDistrict_QNAME)) {
                        ret.setSchoolDistrict(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ServiceType_QNAME)) {
                        ret.setServiceType(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_SexualOrientation_QNAME)) {
                        ret.setSexualOrientation(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Shipping_QNAME)) {
                        ret.getShipping().add(((JAXBElement<ShippingType>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Size_QNAME)) {
                        ret.getSize().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_SquareFootage_QNAME)) {
                        ret.getSquareFootage().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Subject_QNAME)) {
                        ret.getSubject().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_SubjectArea_QNAME)) {
                        ret.getSubjectArea().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_TaxPercent_QNAME)) {
                        ret.setTaxPercent(((JAXBElement<BigDecimal>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_TaxRegion_QNAME)) {
                        ret.setTaxRegion(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_ToLocation_QNAME)) {
                        ret.setToLocation(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_TravelDateRange_QNAME)) {
                        DateTimeRangeType d = ((JAXBElement<DateTimeRangeType>) u.unmarshal(e)).getValue();
                        ret.setTravelDateRange(d);
                        it.remove();
                    } else if (same(name, GOOGLEBASE_University_QNAME)) {
                        ret.setUniversity(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Upc_QNAME)) {
                        ret.setUpc(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_UrlOfItemReviewed_QNAME)) {
                        ret.setUrlOfItemReviewed(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_VehicleType_QNAME)) {
                        ret.getVehicleType().add(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Vin_QNAME)) {
                        ret.setVin(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Weight_QNAME)) {
                        ret.setWeight(e.getTextContent());
                        it.remove();
                    } else if (same(name, GOOGLEBASE_Year_QNAME)) {
                        ret.setYear(((JAXBElement<XMLGregorianCalendar>) u.unmarshal(e)).getValue());
                        it.remove();
                    } else if (HTTP_BASE_GOOGLE_COM_CNS_1_0.equals(e.getNamespaceURI())) {
                        ret.getAny().add(e);
                        it.remove();
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the Georss extension elements from the input list into an {@link GeoRssExtension}
   * object.<br/>
   * The extracted elements will be removed from the original input list.
   * 
   * <br/>
   * see http://georss.org/ about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link GeoRssExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static GeoRssExtension extractGeoRssExtension(List<Element> otherElements) throws YarfrawException {
        GeoRssExtension ret = new GeoRssExtension();
        try {
            if (otherElements != null) {
                Unmarshaller u = JAXBContext.newInstance(GEORSS_JAXB_CONTEXT).createUnmarshaller();
                Iterator<Element> it = otherElements.iterator();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, GEORSS_WHERE_QNAME)) {
                        Where copy = ((JAXBElement<Where>) u.unmarshal(e)).getValue();
                        ret.setWhere(copy);
                        it.remove();
                    } else if (same(name, GEORSS_BOX_QNAME)) {
                        SimplePositionType pos = ((JAXBElement<SimplePositionType>) u.unmarshal(e)).getValue();
                        ret.setBox(pos);
                        it.remove();
                    } else if (same(name, GEORSS_LINE_QNAME)) {
                        SimplePositionType pos = ((JAXBElement<SimplePositionType>) u.unmarshal(e)).getValue();
                        ret.setLine(pos);
                        it.remove();
                    } else if (same(name, GEORSS_POINT_QNAME)) {
                        SimplePositionType pos = ((JAXBElement<SimplePositionType>) u.unmarshal(e)).getValue();
                        ret.setPoint(pos);
                        it.remove();
                    } else if (same(name, GEORSS_POLYGON_QNAME)) {
                        SimplePositionType pos = ((JAXBElement<SimplePositionType>) u.unmarshal(e)).getValue();
                        ret.setPolygon(pos);
                        it.remove();
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Extracts the mrss extension elements from the input list into an {@link MrssExtension}
   * object.<br/>
   * The extracted elements will be removed from the original input list.
   * 
   * <br/>
   * see http://search.yahoo.com/mrss about these
   * extension elements
   * @param otherElements - any elements
   * @return an {@link MrssExtension} object
   * @throws YarfrawException 
   */
    @SuppressWarnings("unchecked")
    public static MrssExtension extractMrssExtension(List<Element> otherElements) throws YarfrawException {
        MrssExtension ret = new MrssExtension();
        try {
            if (otherElements != null) {
                Unmarshaller u = JAXBContext.newInstance(MRSS_JAXB_CONTEXT).createUnmarshaller();
                Iterator<Element> it = otherElements.iterator();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e == null) {
                        continue;
                    }
                    QName name = new QName(e.getNamespaceURI(), e.getLocalName());
                    if (same(name, MRSS_CATEGORY_QNAME)) {
                        MrssCategoryType cat = ((JAXBElement<MrssCategoryType>) u.unmarshal(e)).getValue();
                        ret.getCategory().add(cat);
                        it.remove();
                    } else if (same(name, MRSS_CONTENT_QNAME)) {
                        MrssContentType content = ((JAXBElement<MrssContentType>) u.unmarshal(e)).getValue();
                        ret.getContent().add(content);
                        it.remove();
                    } else if (same(name, MRSS_COPYRIGHT_QNAME)) {
                        MrssCopyrightType copy = ((JAXBElement<MrssCopyrightType>) u.unmarshal(e)).getValue();
                        ret.setCopyright(copy);
                        it.remove();
                    } else if (same(name, MRSS_CREDIT_QNAME)) {
                        MrssCreditType copy = ((JAXBElement<MrssCreditType>) u.unmarshal(e)).getValue();
                        ret.getCredit().add(copy);
                        it.remove();
                    } else if (same(name, MRSS_DESCRIPTION_QNAME)) {
                        MrssDescriptionType copy = ((JAXBElement<MrssDescriptionType>) u.unmarshal(e)).getValue();
                        ret.setDescription(copy);
                        it.remove();
                    } else if (same(name, MRSS_GROUP_QNAME)) {
                        MrssGroupType copy = ((JAXBElement<MrssGroupType>) u.unmarshal(e)).getValue();
                        ret.getGroup().add(copy);
                        it.remove();
                    } else if (same(name, MRSS_HASH_QNAME)) {
                        MrssHashType copy = ((JAXBElement<MrssHashType>) u.unmarshal(e)).getValue();
                        ret.setHash(copy);
                        it.remove();
                    } else if (same(name, MRSS_KEYWORDS_QNAME)) {
                        ret.setKeywords(e.getTextContent());
                        it.remove();
                    } else if (same(name, MRSS_PLAYER_QNAME)) {
                        MrssPlayerType copy = ((JAXBElement<MrssPlayerType>) u.unmarshal(e)).getValue();
                        ret.setPlayer(copy);
                        it.remove();
                    } else if (same(name, MRSS_RATING_QNAME)) {
                        MrssRatingType copy = ((JAXBElement<MrssRatingType>) u.unmarshal(e)).getValue();
                        ret.setRating(copy);
                        it.remove();
                    } else if (same(name, MRSS_RESTRICTION_QNAME)) {
                        MrssRestrictionType copy = ((JAXBElement<MrssRestrictionType>) u.unmarshal(e)).getValue();
                        ret.setRestriction(copy);
                        it.remove();
                    } else if (same(name, MRSS_TEXT_QNAME)) {
                        MrssTextType copy = ((JAXBElement<MrssTextType>) u.unmarshal(e)).getValue();
                        ret.setText(copy);
                        it.remove();
                    } else if (same(name, MRSS_THUMBNAIL_QNAME)) {
                        MrssThumbnailType copy = ((JAXBElement<MrssThumbnailType>) u.unmarshal(e)).getValue();
                        ret.setThumbnail(copy);
                        it.remove();
                    } else if (same(name, MRSS_TITLE_QNAME)) {
                        MrssTitleType copy = ((JAXBElement<MrssTitleType>) u.unmarshal(e)).getValue();
                        ret.setTitle(copy);
                        it.remove();
                    }
                }
            }
        } catch (JAXBException e) {
            throw new YarfrawException("unable to unmarshal element", e);
        }
        return ret;
    }

    /**
   * Converts the input {@link ItunesExtension} object to an element list.
   * <br/>
   * see http://www.apple.com/itunes/store/podcaststechspecs.html about these
   * extension elements
   * 
   * @param extensionObject an valid {@link ItunesExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toItunesElements(ItunesExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.ITUNES);
    }

    /**
   * Converts the input {@link GeoRssExtension} object to an element list.
   * <br/>
   * see http://georss.org/ about these
   * extension elements
   * 
   * @param extensionObject an valid {@link GeoRssExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toGeoRssElements(GeoRssExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.GEORSS);
    }

    /**
   * Converts the input {@link GoogleBaseExtension} object to an element list.
   * <br/>
   * see http://base.google.com/support/bin/answer.py?answer=58085&hl=en about these
   * extension elements
   * 
   * @param extensionObject an valid {@link GoogleBaseExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toGoogleBaseElements(GoogleBaseExtension extensionObject) throws YarfrawException {
        JAXBElement<GoogleBaseExtension> jaxb = GOOGLEBASE_FACTORY.createGoogleBaseExtension(extensionObject);
        return toElements(jaxb, ContextEnum.GOOGLEBASE);
    }

    /**
   * Converts the input {@link MrssExtension} object to an element list.
   * <br/>
   * see http://search.yahoo.com/mrss about these
   * extension elements
   * 
   * @param extensionObject an valid {@link MrssExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toMrssElements(MrssExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.MRSS);
    }

    /**
   * Converts the input {@link WellFormedWebExtension} object to an element list.
   * <br/>
   * see http://wellformedweb.org/news/wfw_namespace_elements about these
   * extension elements
   * 
   * @param extensionObject an valid {@link WellFormedWebExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toWellFormedWebElements(WellFormedWebExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.WFW);
    }

    /**
   * Converts the input {@link DublinCoreExtension} object to an element list.
   * <br/>
   * see http://dublincore.org/documents/2002/07/31/dcmes-xml/ about these
   * extension elements
   * 
   * @param extensionObject an valid {@link DublinCoreExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toDublinCoreElements(DublinCoreExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.DC);
    }

    /**
   * Converts the input {@link SyndicationExtension} object to an element list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/syndication/ about these
   * extension elements
   * 
   * @param extensionObject an valid {@link SyndicationExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toSyndicationElements(SyndicationExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.SY);
    }

    /**
   * Converts the input {@link BloggerExtension} object to an element list.
   * <br/>
   * see http://code.blogger.com/archives/atom-docs.html#extensions about these
   * extension elements
   * 
   * @param extensionObject an valid {@link BloggerExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toBloggerAtomElements(BloggerExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.BLOGGER);
    }

    /**
   * Converts the input {@link AdminExtension} object to an element list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/admin/ about these
   * extension elements
   * 
   * @param extensionObject an valid {@link AdminExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toAdminAtomElements(AdminExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.ADMIN);
    }

    /**
   * Converts the input {@link FeedburnerExtension} object to an element list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/admin/ about these
   * extension elements
   * 
   * @param extensionObject an valid {@link FeedburnerExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toFeedburnerElements(FeedburnerExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.FEEDBURNER);
    }

    /**
   * Converts the input {@link FeedburnerExtension} object to an element list.
   * <br/>
   * see http://web.resource.org/rss/1.0/modules/admin/ about these
   * extension elements
   * 
   * @param extensionObject an valid {@link FeedburnerExtension} object
   * @return a list of elements representing all the elements in the input extension object
   * @throws YarfrawException if conversion failed
   */
    public static List<Element> toSlahsElements(SlashExtension extensionObject) throws YarfrawException {
        return toElements(extensionObject, ContextEnum.SLASH);
    }

    private static JAXBContext DC_CTX = null;

    private static JAXBContext GEORSS_CTX = null;

    private static JAXBContext GOOGLEBASE_CTX = null;

    private static JAXBContext ITUNES_CTX = null;

    private static JAXBContext MRSS_CTX = null;

    private static JAXBContext SY_CTX = null;

    private static JAXBContext WFW_CTX = null;

    private static JAXBContext BLOGGER_CTX = null;

    private static JAXBContext ADMIN_CTX = null;

    private static JAXBContext FEEDBURNER_CTX = null;

    private static JAXBContext SLASH_CTX = null;

    private static JAXBContext ATOM03_CTX = null;

    private static synchronized JAXBContext getContext(ContextEnum ctxEnum) throws JAXBException {
        if (ctxEnum == ContextEnum.DC) {
            if (DC_CTX == null) {
                DC_CTX = JAXBContext.newInstance(DUBLINCORE_JAXB_CONTEXT);
            }
            return DC_CTX;
        } else if (ctxEnum == ContextEnum.ATOM03) {
            if (ATOM03_CTX == null) {
                ATOM03_CTX = JAXBContext.newInstance(ATOM03_JAXB_CONTEXT);
            }
            return ATOM03_CTX;
        } else if (ctxEnum == ContextEnum.ADMIN) {
            if (ADMIN_CTX == null) {
                ADMIN_CTX = JAXBContext.newInstance(ADMIN_JAXB_CONTEXT);
            }
            return ADMIN_CTX;
        } else if (ctxEnum == ContextEnum.BLOGGER) {
            if (BLOGGER_CTX == null) {
                BLOGGER_CTX = JAXBContext.newInstance(BLOGGER_JAXB_CONTEXT);
            }
            return BLOGGER_CTX;
        } else if (ctxEnum == ContextEnum.FEEDBURNER) {
            if (FEEDBURNER_CTX == null) {
                FEEDBURNER_CTX = JAXBContext.newInstance(FEEDBURNER_JAXB_CONTEXT);
            }
            return FEEDBURNER_CTX;
        } else if (ctxEnum == ContextEnum.SLASH) {
            if (SLASH_CTX == null) {
                SLASH_CTX = JAXBContext.newInstance(SLASH_JAXB_CONTEXT);
            }
            return SLASH_CTX;
        } else if (ctxEnum == ContextEnum.GEORSS) {
            if (GEORSS_CTX == null) {
                GEORSS_CTX = JAXBContext.newInstance(GEORSS_JAXB_CONTEXT);
            }
            return GEORSS_CTX;
        } else if (ctxEnum == ContextEnum.GOOGLEBASE) {
            if (GOOGLEBASE_CTX == null) {
                GOOGLEBASE_CTX = JAXBContext.newInstance(GOOGLEBASE_JAXB_CONTEXT);
            }
            return GOOGLEBASE_CTX;
        } else if (ctxEnum == ContextEnum.ITUNES) {
            if (ITUNES_CTX == null) {
                ITUNES_CTX = JAXBContext.newInstance(ITUNES_JAXB_CONTEXT);
            }
            return ITUNES_CTX;
        } else if (ctxEnum == ContextEnum.MRSS) {
            if (MRSS_CTX == null) {
                MRSS_CTX = JAXBContext.newInstance(MRSS_JAXB_CONTEXT);
            }
            return MRSS_CTX;
        } else if (ctxEnum == ContextEnum.SY) {
            if (SY_CTX == null) {
                SY_CTX = JAXBContext.newInstance(SY_JAXB_CONTEXT);
            }
            return SY_CTX;
        } else if (ctxEnum == ContextEnum.WFW) {
            if (WFW_CTX == null) {
                WFW_CTX = JAXBContext.newInstance(WFW_JAXB_CONTEXT);
            }
            return WFW_CTX;
        }
        throw new UnsupportedOperationException("Unknown JAXB context: " + ctxEnum);
    }

    private static List<Element> toElements(Object extensionObject, ContextEnum ctxEnum) throws YarfrawException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        List<Element> ret = new ArrayList<Element>();
        try {
            Document doc = dbf.newDocumentBuilder().newDocument();
            Marshaller m = getContext(ctxEnum).createMarshaller();
            m.setProperty(JAXBUtils.PREFIX_MAPPER_PROPERTY_NAME, JAXBUtils.getNamespacePrefixMapper(FeedFormat.RSS20));
            m.marshal(extensionObject, doc);
            Element e = doc.getDocumentElement();
            NodeList list = e.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);
                if (n instanceof Element) {
                    Element element = (Element) n;
                    ret.add(element);
                } else {
                    LOG.error("Ignore unexpected node " + n.getNodeName() + ", this should not happen");
                }
            }
        } catch (ParserConfigurationException e) {
            throw new YarfrawException("Parserconfig exception", e);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new YarfrawException("JAXB exception", e);
        }
        return ret;
    }
}
