package org.isurf.gdssu.datapool.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.isurf.gdssu.datapool.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _GetTradeItemInformation_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "getTradeItemInformation");

    private static final QName _FindPartyName_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "findPartyName");

    private static final QName _NoticationList_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "noticationList");

    private static final QName _ManageItemResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "manageItemResponse");

    private static final QName _FindPartyNameResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "findPartyNameResponse");

    private static final QName _PublishItem_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "publishItem");

    private static final QName _SubscribeParty_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "subscribeParty");

    private static final QName _ManageParty_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "manageParty");

    private static final QName _PublishItemResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "publishItemResponse");

    private static final QName _GlobalSearchResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "globalSearchResponse");

    private static final QName _ManageItem_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "manageItem");

    private static final QName _ManagePartyResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "managePartyResponse");

    private static final QName _SubscribePartyResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "subscribePartyResponse");

    private static final QName _GetTradeItemInformationResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "getTradeItemInformationResponse");

    private static final QName _NoticationListResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "noticationListResponse");

    private static final QName _SubscribeRetailerPartyResponse_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "subscribeRetailerPartyResponse");

    private static final QName _SubscribeRetailerParty_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "subscribeRetailerParty");

    private static final QName _GlobalSearch_QNAME = new QName("http://ws.datapool.gdssu.isurf.org/", "globalSearch");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.isurf.gdssu.datapool.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NoticationList }
     * 
     */
    public NoticationList createNoticationList() {
        return new NoticationList();
    }

    /**
     * Create an instance of {@link ManageItem }
     * 
     */
    public ManageItem createManageItem() {
        return new ManageItem();
    }

    /**
     * Create an instance of {@link Identifier }
     * 
     */
    public Identifier createIdentifier() {
        return new Identifier();
    }

    /**
     * Create an instance of {@link SubscribeRetailerPartyResponse }
     * 
     */
    public SubscribeRetailerPartyResponse createSubscribeRetailerPartyResponse() {
        return new SubscribeRetailerPartyResponse();
    }

    /**
     * Create an instance of {@link GetTradeItemInformation }
     * 
     */
    public GetTradeItemInformation createGetTradeItemInformation() {
        return new GetTradeItemInformation();
    }

    /**
     * Create an instance of {@link GlobalSearch }
     * 
     */
    public GlobalSearch createGlobalSearch() {
        return new GlobalSearch();
    }

    /**
     * Create an instance of {@link FindPartyName }
     * 
     */
    public FindPartyName createFindPartyName() {
        return new FindPartyName();
    }

    /**
     * Create an instance of {@link ManageParty }
     * 
     */
    public ManageParty createManageParty() {
        return new ManageParty();
    }

    /**
     * Create an instance of {@link ManagePartyResponse }
     * 
     */
    public ManagePartyResponse createManagePartyResponse() {
        return new ManagePartyResponse();
    }

    /**
     * Create an instance of {@link FindPartyNameResponse }
     * 
     */
    public FindPartyNameResponse createFindPartyNameResponse() {
        return new FindPartyNameResponse();
    }

    /**
     * Create an instance of {@link PublishItem }
     * 
     */
    public PublishItem createPublishItem() {
        return new PublishItem();
    }

    /**
     * Create an instance of {@link SubscribeParty }
     * 
     */
    public SubscribeParty createSubscribeParty() {
        return new SubscribeParty();
    }

    /**
     * Create an instance of {@link NoticationListResponse }
     * 
     */
    public NoticationListResponse createNoticationListResponse() {
        return new NoticationListResponse();
    }

    /**
     * Create an instance of {@link PublishItemResponse }
     * 
     */
    public PublishItemResponse createPublishItemResponse() {
        return new PublishItemResponse();
    }

    /**
     * Create an instance of {@link GetTradeItemInformationResponse }
     * 
     */
    public GetTradeItemInformationResponse createGetTradeItemInformationResponse() {
        return new GetTradeItemInformationResponse();
    }

    /**
     * Create an instance of {@link GlobalSearchResponse }
     * 
     */
    public GlobalSearchResponse createGlobalSearchResponse() {
        return new GlobalSearchResponse();
    }

    /**
     * Create an instance of {@link ManageItemResponse }
     * 
     */
    public ManageItemResponse createManageItemResponse() {
        return new ManageItemResponse();
    }

    /**
     * Create an instance of {@link SubscribeRetailerParty }
     * 
     */
    public SubscribeRetailerParty createSubscribeRetailerParty() {
        return new SubscribeRetailerParty();
    }

    /**
     * Create an instance of {@link SubscribePartyResponse }
     * 
     */
    public SubscribePartyResponse createSubscribePartyResponse() {
        return new SubscribePartyResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTradeItemInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "getTradeItemInformation")
    public JAXBElement<GetTradeItemInformation> createGetTradeItemInformation(GetTradeItemInformation value) {
        return new JAXBElement<GetTradeItemInformation>(_GetTradeItemInformation_QNAME, GetTradeItemInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPartyName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "findPartyName")
    public JAXBElement<FindPartyName> createFindPartyName(FindPartyName value) {
        return new JAXBElement<FindPartyName>(_FindPartyName_QNAME, FindPartyName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoticationList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "noticationList")
    public JAXBElement<NoticationList> createNoticationList(NoticationList value) {
        return new JAXBElement<NoticationList>(_NoticationList_QNAME, NoticationList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "manageItemResponse")
    public JAXBElement<ManageItemResponse> createManageItemResponse(ManageItemResponse value) {
        return new JAXBElement<ManageItemResponse>(_ManageItemResponse_QNAME, ManageItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindPartyNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "findPartyNameResponse")
    public JAXBElement<FindPartyNameResponse> createFindPartyNameResponse(FindPartyNameResponse value) {
        return new JAXBElement<FindPartyNameResponse>(_FindPartyNameResponse_QNAME, FindPartyNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PublishItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "publishItem")
    public JAXBElement<PublishItem> createPublishItem(PublishItem value) {
        return new JAXBElement<PublishItem>(_PublishItem_QNAME, PublishItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscribeParty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "subscribeParty")
    public JAXBElement<SubscribeParty> createSubscribeParty(SubscribeParty value) {
        return new JAXBElement<SubscribeParty>(_SubscribeParty_QNAME, SubscribeParty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageParty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "manageParty")
    public JAXBElement<ManageParty> createManageParty(ManageParty value) {
        return new JAXBElement<ManageParty>(_ManageParty_QNAME, ManageParty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PublishItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "publishItemResponse")
    public JAXBElement<PublishItemResponse> createPublishItemResponse(PublishItemResponse value) {
        return new JAXBElement<PublishItemResponse>(_PublishItemResponse_QNAME, PublishItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalSearchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "globalSearchResponse")
    public JAXBElement<GlobalSearchResponse> createGlobalSearchResponse(GlobalSearchResponse value) {
        return new JAXBElement<GlobalSearchResponse>(_GlobalSearchResponse_QNAME, GlobalSearchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "manageItem")
    public JAXBElement<ManageItem> createManageItem(ManageItem value) {
        return new JAXBElement<ManageItem>(_ManageItem_QNAME, ManageItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManagePartyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "managePartyResponse")
    public JAXBElement<ManagePartyResponse> createManagePartyResponse(ManagePartyResponse value) {
        return new JAXBElement<ManagePartyResponse>(_ManagePartyResponse_QNAME, ManagePartyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscribePartyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "subscribePartyResponse")
    public JAXBElement<SubscribePartyResponse> createSubscribePartyResponse(SubscribePartyResponse value) {
        return new JAXBElement<SubscribePartyResponse>(_SubscribePartyResponse_QNAME, SubscribePartyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTradeItemInformationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "getTradeItemInformationResponse")
    public JAXBElement<GetTradeItemInformationResponse> createGetTradeItemInformationResponse(GetTradeItemInformationResponse value) {
        return new JAXBElement<GetTradeItemInformationResponse>(_GetTradeItemInformationResponse_QNAME, GetTradeItemInformationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoticationListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "noticationListResponse")
    public JAXBElement<NoticationListResponse> createNoticationListResponse(NoticationListResponse value) {
        return new JAXBElement<NoticationListResponse>(_NoticationListResponse_QNAME, NoticationListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscribeRetailerPartyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "subscribeRetailerPartyResponse")
    public JAXBElement<SubscribeRetailerPartyResponse> createSubscribeRetailerPartyResponse(SubscribeRetailerPartyResponse value) {
        return new JAXBElement<SubscribeRetailerPartyResponse>(_SubscribeRetailerPartyResponse_QNAME, SubscribeRetailerPartyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubscribeRetailerParty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "subscribeRetailerParty")
    public JAXBElement<SubscribeRetailerParty> createSubscribeRetailerParty(SubscribeRetailerParty value) {
        return new JAXBElement<SubscribeRetailerParty>(_SubscribeRetailerParty_QNAME, SubscribeRetailerParty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GlobalSearch }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.datapool.gdssu.isurf.org/", name = "globalSearch")
    public JAXBElement<GlobalSearch> createGlobalSearch(GlobalSearch value) {
        return new JAXBElement<GlobalSearch>(_GlobalSearch_QNAME, GlobalSearch.class, null, value);
    }
}
