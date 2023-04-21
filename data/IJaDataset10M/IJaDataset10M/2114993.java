package org.openXpertya.fastrack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import org.openXpertya.model.MAcctProcessor;
import org.openXpertya.model.MBPGroup;
import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MBPartnerLocation;
import org.openXpertya.model.MClient;
import org.openXpertya.model.MDiscountSchema;
import org.openXpertya.model.MDiscountSchemaBreak;
import org.openXpertya.model.MDiscountSchemaLine;
import org.openXpertya.model.MFormAccess;
import org.openXpertya.model.MLocation;
import org.openXpertya.model.MLocator;
import org.openXpertya.model.MOrg;
import org.openXpertya.model.MPaySchedule;
import org.openXpertya.model.MPaymentTerm;
import org.openXpertya.model.MPriceList;
import org.openXpertya.model.MPriceListVersion;
import org.openXpertya.model.MProcessAccess;
import org.openXpertya.model.MProduct;
import org.openXpertya.model.MProductCategory;
import org.openXpertya.model.MRetSchemaConfig;
import org.openXpertya.model.MRetencionProcessor;
import org.openXpertya.model.MRetencionSchema;
import org.openXpertya.model.MRetencionType;
import org.openXpertya.model.MRole;
import org.openXpertya.model.MRoleOrgAccess;
import org.openXpertya.model.MSequence;
import org.openXpertya.model.MTabAccess;
import org.openXpertya.model.MTax;
import org.openXpertya.model.MTaxCategory;
import org.openXpertya.model.MTree;
import org.openXpertya.model.MTreeBar;
import org.openXpertya.model.MTree_NodeMM;
import org.openXpertya.model.MUOM;
import org.openXpertya.model.MUOMConversion;
import org.openXpertya.model.MUOMGroup;
import org.openXpertya.model.MUser;
import org.openXpertya.model.MUserRoles;
import org.openXpertya.model.MWarehouse;
import org.openXpertya.model.MWindowAccess;
import org.openXpertya.model.PO;
import org.openXpertya.model.X_AD_TreeBar;
import org.openXpertya.print.MPrintForm;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.wf.MWorkflowAccess;

public class FTClient extends FTModule {

    /** Configuración de la creación del cliente y su configuración */
    private FTConfiguration ftConfig;

    private static final int USER_SUPERVISOR_ID = 100;

    /** 
	 * Colección clave valor que determina los roles creados con los roles template 
	 *  <ul>
	 *  <li>Clave: id del rol template</li>
	 *  <li>Valor: MRole creado nuevo</li>
	 *  </ul> 
	 */
    private HashMap roles = new HashMap();

    /** 
	 * Colección clave valor que determina las organizaciones creadas con las organizaciones template 
	 *  <ul>
	 *  <li>Clave: id de la org template</li>
	 *  <li>Valor: MOrg creado nuevo</li>
	 *  </ul> 
	 */
    private HashMap<Integer, MUser> users = new HashMap<Integer, MUser>();

    /** 
	 * Colección clave valor que almacena los usuarios creados a partir de la compañía template 
	 *  <ul>
	 *  <li>Clave: id del usuario cia template</li>
	 *  <li>Valor: MUser creado nuevo</li>
	 *  </ul> 
	 */
    private HashMap orgs = new HashMap();

    /** 
	 * Colección clave valor que determina los MTree creados con las organizaciones template 
	 *  <ul>
	 *  <li>Clave: id del tree template</li>
	 *  <li>Valor: MTree creado nuevo</li>
	 *  </ul> 
	 */
    private HashMap<Integer, Integer> trees = new HashMap<Integer, Integer>();

    public HashMap<Integer, Integer> getTrees() {
        return trees;
    }

    public void setTrees(HashMap<Integer, Integer> trees) {
        this.trees = trees;
    }

    /** 
	 * Colección que contiene el listado de accesos directos a las entradas del menu
	 * Las entradas corresponden a las contenidas en la plantilla por el usuario Supervisor  
	 */
    private ArrayList<X_AD_TreeBar> treeBarEntries = new ArrayList();

    public ArrayList<X_AD_TreeBar> getTreeBarEntries() {
        return treeBarEntries;
    }

    public void setTreeBarEntries(ArrayList<X_AD_TreeBar> treeBarEntries) {
        this.treeBarEntries = treeBarEntries;
    }

    /** Contexto */
    private Properties ctx = Env.getCtx();

    public FTClient() {
    }

    public FTClient(FTConfiguration ftConfig) {
        this.setTrxName(ftConfig.getTrxName());
        this.setFtConfig(ftConfig);
    }

    public int getClientTemplate() {
        return this.getFtConfig().getClient_template_id();
    }

    public int getNewClient() {
        return this.getFtConfig().getNew_client_id();
    }

    public void setRoles(HashMap roles) {
        this.roles = roles;
    }

    public HashMap getRoles() {
        return roles;
    }

    public void setOrgs(HashMap orgs) {
        this.orgs = orgs;
    }

    public HashMap getOrgs() {
        return orgs;
    }

    public void setCtx(Properties ctx) {
        this.ctx = ctx;
    }

    public Properties getCtx() {
        return ctx;
    }

    public void setFtConfig(FTConfiguration ftConfig) {
        this.ftConfig = ftConfig;
    }

    public FTConfiguration getFtConfig() {
        return ftConfig;
    }

    private int getOrgId(int AD_Org_ID) {
        int org = 0;
        if (this.getOrgs().containsKey(AD_Org_ID)) {
            org = ((MOrg) this.getOrgs().get(AD_Org_ID)).getID();
        }
        return org;
    }

    private void setCtx(int AD_Client_ID) {
        Env.setContext(this.getCtx(), "#AD_Client_ID", AD_Client_ID);
    }

    private void setCtx(int AD_Client_ID, int AD_Org_ID) {
        this.setCtx(AD_Client_ID);
        Env.setContext(this.getCtx(), "#AD_Org_ID", AD_Org_ID);
    }

    private void createInitialInfo() throws Exception {
        this.createOrgs();
        this.createRoles();
        this.createMenuAccess();
        this.createRoleOrgAccess();
        this.createPrintForm();
        this.createTreeBarEntries();
    }

    /**
	 * Carga las entradas de accesos directos del menú
	 */
    private void createTreeBarEntries() {
        String sql = "";
        try {
            sql = "SELECT * FROM AD_TREEBAR WHERE AD_CLIENT_ID = " + getClientTemplate();
            PreparedStatement stmt = DB.prepareStatement(sql, getTrxName());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) treeBarEntries.add(new X_AD_TreeBar(getCtx(), rs, getTrxName()));
        } catch (Exception e) {
            this.getLog().log(Level.SEVERE, sql, e);
        }
    }

    /**
	 * Realiza la configuración de la compañía para que sea utilizable 
	 */
    private void configureClient() throws Exception {
        this.setCtx(this.getNewClient(), 0);
        Map<Integer, Integer> taxCategories = new HashMap<Integer, Integer>();
        Map<Integer, Integer> locations = new HashMap<Integer, Integer>();
        MProductCategory pc = new MProductCategory(this.getCtx(), 0, this.getTrxName());
        pc.setValue("Standard");
        pc.setName("Standard");
        pc.setIsDefault(true);
        pc.save();
        this.createTaxCategories(taxCategories);
        int c_uom_id = this.createUOMs();
        MProduct product = new MProduct(this.getCtx(), 0, this.getTrxName());
        product.setValue("Standard");
        product.setName("Standard");
        product.setM_Product_Category_ID(pc.getID());
        product.setC_TaxCategory_ID(taxCategories.values().iterator().next());
        product.setC_UOM_ID((c_uom_id == 0) ? 100 : c_uom_id);
        product.save();
        this.createDiscountSchemaAndPriceLists();
        this.createPaymentTerm();
        this.createAccountingProcessor();
        this.createWarehousesAndLocators(locations);
        this.createInfoRetenciones(taxCategories, c_uom_id, pc.getID(), locations);
    }

    /**
	 * Crea la info de retenciones a partir de la compañía template
	 * @param taxCategories categorías de impuesto para los productos de los tipos de retención
	 * @param c_uom_id uom id para los productos de los tipos de retención
	 * @param productCategory categoria de producto para los productos de los tipos de retención
	 * @throws Exception
	 */
    private void createInfoRetenciones(Map<Integer, Integer> taxCategories, int c_uom_id, int productCategory, Map<Integer, Integer> locations) throws Exception {
        Map<Integer, Integer> retenc_procesadores = new HashMap<Integer, Integer>();
        Map<Integer, Integer> retenc_types = new HashMap<Integer, Integer>();
        Map<Integer, Integer> entes_recaudadores = new HashMap<Integer, Integer>();
        Map<Integer, Integer> grupo_entes = new HashMap<Integer, Integer>();
        this.createRetencionTypes(retenc_types, taxCategories, c_uom_id, productCategory);
        this.createRetencionProcesador(retenc_procesadores);
        this.setCtx(this.getClientTemplate());
        List<MRetencionSchema> esquemas = MRetencionSchema.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MRetencionSchema newEsquema;
        int id_ente;
        for (MRetencionSchema retencionSchema : esquemas) {
            newEsquema = new MRetencionSchema(this.getCtx(), 0, this.getTrxName());
            if (!entes_recaudadores.containsKey(retencionSchema.getC_BPartner_Recaudador_ID())) {
                id_ente = this.createEnteRecaudador(retencionSchema.getC_BPartner_Recaudador_ID(), grupo_entes, locations);
                entes_recaudadores.put(retencionSchema.getC_BPartner_Recaudador_ID(), id_ente);
            } else {
                id_ente = entes_recaudadores.get(retencionSchema.getC_BPartner_Recaudador_ID());
            }
            MRetencionSchema.copyValues(retencionSchema, newEsquema);
            newEsquema.setC_RetencionProcessor_ID(retenc_procesadores.get(retencionSchema.getC_RetencionProcessor_ID()));
            newEsquema.setC_RetencionType_ID(retenc_types.get(retencionSchema.getC_RetencionType_ID()));
            newEsquema.setC_BPartner_Recaudador_ID(id_ente);
            newEsquema.save();
            this.createEsquemaParams(retencionSchema, newEsquema);
        }
    }

    /**
	 * Crea el nuevo bpartner recaudador a partir del template
	 * @param bpartner_template_id bpartner template
	 * @param grupos asociacion entre los grupos template y los nuevos
	 * @param locations asociacion entre las locations template y las nuevas
	 * @return id del bpartner recaudador nuevo
	 * @throws Exception
	 */
    private int createEnteRecaudador(Integer bpartner_template_id, Map<Integer, Integer> grupos, Map<Integer, Integer> locations) throws Exception {
        this.setCtx(this.getNewClient());
        MBPartner newPartner = new MBPartner(this.getCtx(), 0, this.getTrxName());
        MBPartner partner = new MBPartner(this.getCtx(), bpartner_template_id, this.getTrxName());
        int bgroup_id = groupsRecaudador(partner, grupos);
        MBPartner.copyValues(partner, newPartner);
        newPartner.setC_BP_Group_ID(bgroup_id);
        newPartner.save();
        this.createPartnerLocations(partner, newPartner, locations);
        return newPartner.getID();
    }

    /**
	 * Crea las bpartner locations de la template
	 * @param partner partner template
	 * @param newPartner nuevo partner
	 * @param locations asociacion de las locations template con las creadas
	 */
    private void createPartnerLocations(MBPartner partner, MBPartner newPartner, Map<Integer, Integer> locations) throws Exception {
        MBPartnerLocation[] bpLocations = partner.getLocations(true);
        MBPartnerLocation newBPLocation;
        for (MBPartnerLocation partnerLocation : bpLocations) {
            newBPLocation = new MBPartnerLocation(this.getCtx(), 0, this.getTrxName());
            MBPartnerLocation.copyValues(partnerLocation, newBPLocation);
            newBPLocation.setC_Location_ID(this.createLocation(partnerLocation));
            newBPLocation.setC_BPartner_ID(newPartner.getID());
            newBPLocation.save();
        }
    }

    /**
	 * Crea las locations del bpartner location
	 * @param bpartnerLocation bpartner location template
	 * @return id del location nuevo
	 * @throws Exception
	 */
    private int createLocation(MBPartnerLocation bpartnerLocation) throws Exception {
        MLocation locationTemplate = new MLocation(this.getCtx(), bpartnerLocation.getC_Location_ID(), this.getTrxName());
        MLocation newLocation = new MLocation(this.getCtx(), 0, this.getTrxName());
        MLocation.copyValues(locationTemplate, newLocation);
        newLocation.save();
        return newLocation.getID();
    }

    /**
	 * Verifica si está el grupo del recaudador en grupos y si no está lo creo 
	 * @param partner business partner template
	 * @param grupos asociación entre los grupos templates y los nuevos creados
	 * @return id del grupo para asociar al ente recaudador nuevo 
	 * @throws Exception
	 */
    private int groupsRecaudador(MBPartner partner, Map<Integer, Integer> grupos) throws Exception {
        int groupRetorno;
        if (!grupos.containsKey(partner.getC_BP_Group_ID())) {
            groupRetorno = this.createGroupRecaudador(partner);
            grupos.put(partner.getC_BP_Group_ID(), groupRetorno);
        } else {
            groupRetorno = grupos.get(partner.getC_BP_Group_ID()).intValue();
        }
        return groupRetorno;
    }

    /**
	 * Crea el bpartner group a partir del template
	 * @param partner business partner con el grupo template
	 * @return id del grupo nuevo creado
	 * @throws Exception
	 */
    private int createGroupRecaudador(MBPartner partner) throws Exception {
        this.setCtx(this.getNewClient());
        MBPGroup groupTemplate = new MBPGroup(this.getCtx(), partner.getC_BP_Group_ID(), this.getTrxName());
        MBPGroup newGroup = new MBPGroup(this.getCtx(), 0, this.getTrxName());
        MBPGroup.copyValues(groupTemplate, newGroup);
        newGroup.save();
        return newGroup.getID();
    }

    /**
	 * Crea los parámetros del esquema de retencion template al esquema nuevo
	 * @param schemaTemplate esquema template
	 * @param newSchema nuevo esquema
	 */
    private void createEsquemaParams(MRetencionSchema schemaTemplate, MRetencionSchema newSchema) throws Exception {
        this.setCtx(this.getNewClient());
        Collection<Object> params = ((Map<String, Object>) schemaTemplate.getParameters()).values();
        MRetSchemaConfig newParam;
        for (Object paramTemplate : params) {
            newParam = new MRetSchemaConfig(this.getCtx(), 0, this.getTrxName());
            MRetSchemaConfig.copyValues((MRetSchemaConfig) paramTemplate, newParam);
            newParam.setC_RetencionSchema_ID(newSchema.getID());
            newParam.save();
        }
    }

    /**
	 * Creo los tipos de retenciones
	 * @param types hashmap a inicializar con los tipos de retención y las nuevas creadas
	 * @param taxCategories categorias de impuesto para el producto
	 * @param c_uom_id uom id para el producto
	 * @param productCategory subfamilia para el producto 
	 */
    private void createRetencionTypes(Map<Integer, Integer> types, Map<Integer, Integer> taxCategories, int c_uom_id, int productCategory) throws Exception {
        this.setCtx(this.getClientTemplate());
        Map<Integer, Integer> productos = new HashMap<Integer, Integer>();
        List<MRetencionType> list_types = MRetencionType.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MRetencionType newType;
        int product_id;
        for (MRetencionType retencionType : list_types) {
            newType = new MRetencionType(this.getCtx(), 0, this.getTrxName());
            if (!productos.containsKey(retencionType.getM_Product_ID())) {
                product_id = this.createRetencionProduct(retencionType.getM_Product_ID(), taxCategories, c_uom_id, productCategory);
                productos.put(retencionType.getM_Product_ID(), product_id);
            } else {
                product_id = productos.get(retencionType.getM_Product_ID()).intValue();
            }
            MRetencionType.copyValues(retencionType, newType);
            newType.setM_Product_ID(product_id);
            newType.save();
            types.put(retencionType.getID(), newType.getID());
        }
    }

    /**
	 * Crea el producto para el tipo de retención
	 * @param product_template_id id del producto template
	 * @param taxCategories categorías de producto para el producto
	 * @param c_uom_id uom id para el producto
	 * @param productCategory categoría para el producto 
	 * @return id del producto nuevo
	 * @throws Exception
	 */
    private int createRetencionProduct(Integer product_template_id, Map<Integer, Integer> taxCategories, int c_uom_id, int productCategory) throws Exception {
        MProduct prod_template = new MProduct(this.getCtx(), product_template_id, this.getTrxName());
        MProduct newProduct = new MProduct(this.getCtx(), 0, this.getTrxName());
        MProduct.copyValues(prod_template, newProduct);
        newProduct.setC_UOM_ID((c_uom_id == 0) ? 100 : c_uom_id);
        newProduct.setM_Product_Category_ID(productCategory);
        newProduct.setC_TaxCategory_ID(taxCategories.get(prod_template.getC_TaxCategory_ID()));
        newProduct.save();
        return newProduct.getID();
    }

    /**
	 * Creo los procesadores de retenciones
	 * @param procesadores hashmap a inicializar con los procesadroes de retención y las nuevas creadas
	 */
    private void createRetencionProcesador(Map<Integer, Integer> procesadores) throws Exception {
        this.setCtx(this.getClientTemplate());
        List<MRetencionProcessor> list_processors = MRetencionProcessor.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MRetencionProcessor newProcessor;
        for (MRetencionProcessor retencionProcessor : list_processors) {
            newProcessor = new MRetencionProcessor(this.getCtx(), 0, this.getTrxName());
            MRetencionProcessor.copyValues(retencionProcessor, newProcessor);
            newProcessor.save();
            procesadores.put(retencionProcessor.getID(), newProcessor.getID());
        }
    }

    /**
	 * Crea las tarifas a partir de las tarifas template y las retorna en Map con: 
	 * <ul>
	 * <li>Clave = id de tarifa template</li>
	 * <li>Valor = id de la tarifa nueva</li>
	 * </ul> 
	 * @return las asociaciones en un Map
	 * @throws Exception
	 */
    private Map<Integer, Integer> createTarifasTemplateAndNew() throws Exception {
        this.setCtx(this.getClientTemplate());
        Map<Integer, Integer> tarifas = new HashMap<Integer, Integer>();
        List<MPriceList> list = MPriceList.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MPriceList newPriceList;
        for (MPriceList priceList : list) {
            newPriceList = new MPriceList(this.getCtx(), 0, this.getTrxName());
            MPriceList.copyValues(priceList, newPriceList);
            newPriceList.setC_Currency_ID(this.getFtConfig().getC_currency_id());
            newPriceList.save();
            tarifas.put(priceList.getID(), newPriceList.getID());
        }
        return tarifas;
    }

    /**
	 * Creo los cortes del esquema de vencimiento template para el esquema nuevo
	 * @param esquema_template
	 * @param id_esquema_nuevo
	 * @throws Exception
	 */
    private void createDiscountsBreaks(MDiscountSchema esquema_template, int id_esquema_nuevo) throws Exception {
        this.setCtx(this.getNewClient());
        MDiscountSchemaBreak[] cortes = esquema_template.getBreaks(true);
        MDiscountSchemaBreak corte;
        for (int i = 0; i < cortes.length; i++) {
            corte = new MDiscountSchemaBreak(this.getCtx(), 0, this.getTrxName());
            MDiscountSchemaBreak.copyValues(cortes[i], corte);
            corte.setM_DiscountSchema_ID(id_esquema_nuevo);
            corte.save();
        }
    }

    /**
	 * Copia las líneas del esquema de descuento pasado como parámetro
	 * @param discountSchema_template
	 * @param newDiscountSchema
	 * @throws Exception
	 */
    private void createDiscountSchemaLines(MDiscountSchema discountSchema_template, int newDiscountSchema) throws Exception {
        MDiscountSchemaLine[] lines = discountSchema_template.getLines(true);
        this.setCtx(this.getNewClient());
        MDiscountSchemaLine newLinea;
        for (int i = 0; i < lines.length; i++) {
            newLinea = new MDiscountSchemaLine(this.getCtx(), 0, this.getTrxName());
            MDiscountSchemaLine.copyValues(lines[i], newLinea);
            newLinea.setM_DiscountSchema_ID(newDiscountSchema);
            newLinea.setC_BPartner_ID(0);
            newLinea.setM_Product_ID(0);
            newLinea.setM_Product_Family_ID(0);
            newLinea.setM_Product_Gamas_ID(0);
            newLinea.setM_Product_Category_ID(0);
            newLinea.save();
        }
    }

    /**
	 * Crea los esquemas de descuento con sus respectivos cortes y lo devuelve en un Map con:
	 * <ul>
	 * <li>Clave = id de tarifa template</li>
	 * <li>Valor = id de la tarifa nueva</li>
	 * </ul>
	 * @return
	 * @throws Exception
	 */
    private Map<Integer, Integer> createDiscountSchemasTemplateAndNew() throws Exception {
        this.setCtx(this.getClientTemplate());
        Map<Integer, Integer> descuentos = new HashMap<Integer, Integer>();
        List<MDiscountSchema> list = MDiscountSchema.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MDiscountSchema newDiscountSchema;
        for (MDiscountSchema discountSchema : list) {
            newDiscountSchema = new MDiscountSchema(this.getCtx(), 0, this.getTrxName());
            MDiscountSchema.copyValues(discountSchema, newDiscountSchema);
            newDiscountSchema.save();
            this.createDiscountsBreaks(discountSchema, newDiscountSchema.getID());
            this.createDiscountSchemaLines(discountSchema, newDiscountSchema.getID());
            descuentos.put(discountSchema.getID(), newDiscountSchema.getID());
        }
        return descuentos;
    }

    /**
     * Crea un impuesto con los datos pasados como parámetro
     * @param name nombre del impuesto
     * @param rate tasa del impuesto
     * @param C_TaxCategory_ID id de la categoría del impuesto a la cual va a pertenecer
     */
    private void createTaxs(int category_origen, int category_destiny) throws Exception {
        this.setCtx(this.getClientTemplate());
        List<MTax> list = MTax.getOfTaxCategory(ctx, category_origen, this.getTrxName());
        HashMap<Integer, Integer> taxs = new HashMap<Integer, Integer>();
        this.setCtx(this.getNewClient());
        MTax newTax;
        for (MTax tax : list) {
            newTax = new MTax(this.getCtx(), 0, this.getTrxName());
            newTax.setIsActive(tax.isActive());
            newTax.setName(tax.getName());
            newTax.setDescription(tax.getDescription());
            newTax.setTaxIndicator(tax.getTaxIndicator());
            newTax.setIsDocumentLevel(tax.isDocumentLevel());
            newTax.setValidFrom(tax.getValidFrom());
            newTax.setIsSummary(tax.isSummary());
            newTax.setRequiresTaxCertificate(tax.isRequiresTaxCertificate());
            newTax.setRate(tax.getRate());
            newTax.setParent_Tax_ID((taxs.get(tax.getID()) == null) ? 0 : (taxs.get(tax.getID())).intValue());
            newTax.setC_TaxCategory_ID(category_destiny);
            newTax.setIsDefault(tax.isDefault());
            newTax.setIsTaxExempt(tax.isTaxExempt());
            newTax.setSOPOType(tax.getSOPOType());
            newTax.setTaxType(tax.getTaxType());
            newTax.setTaxAccusation(tax.getTaxAccusation());
            newTax.save();
            taxs.put(tax.getID(), newTax.getID());
        }
    }

    /**
     * Obtiene las traducciones de la tabla pasada como parámetro
     * @param tableName nombre de la tabla padre
     * @param id id del registro origen de las trl 
     * @throws Exception
     */
    private List<List<Object>> getTrlOfTable(String tableName, int id) throws Exception {
        String sql = "SELECT * FROM " + tableName + "_trl WHERE " + tableName + "_id = " + id;
        return ExecuterSql.executeQueryToList(sql, this.getTrxName());
    }

    /**
	 * Copia las tax categories de la compañia template
	 * @param categories hashmap a inicializar con las categorias de impuesto y las nuevas creadas 
	 * @return el último tax category creado
	 */
    private void createTaxCategories(Map<Integer, Integer> categories) throws Exception {
        this.setCtx(this.getClientTemplate());
        List<MTaxCategory> list = MTaxCategory.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MTaxCategory newTaxCategory;
        for (MTaxCategory taxCategory : list) {
            newTaxCategory = new MTaxCategory(this.getCtx(), 0, this.getTrxName());
            newTaxCategory.setName(taxCategory.getName());
            newTaxCategory.setDescription(taxCategory.getDescription());
            newTaxCategory.setCommodityCode(taxCategory.getCommodityCode());
            newTaxCategory.setIsDefault(taxCategory.isDefault());
            newTaxCategory.setIsActive(taxCategory.isActive());
            newTaxCategory.setIsManual(taxCategory.isManual());
            newTaxCategory.save();
            categories.put(taxCategory.getID(), newTaxCategory.getID());
            this.createTaxs(taxCategory.getID(), newTaxCategory.getID());
        }
    }

    /**
	 * Copia las tarifas, sus versiones y esquemas de vencimiento, con sus respectivos breaks  
	 * @throws Exception
	 */
    private void createDiscountSchemaAndPriceLists() throws Exception {
        Map<Integer, Integer> tarifas = this.createTarifasTemplateAndNew();
        Map<Integer, Integer> descuentos = this.createDiscountSchemasTemplateAndNew();
        this.setCtx(this.getClientTemplate());
        List<MPriceListVersion> versiones = MPriceListVersion.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MPriceListVersion newVersion;
        for (MPriceListVersion version : versiones) {
            newVersion = new MPriceListVersion(this.getCtx(), 0, this.getTrxName());
            MPriceListVersion.copyValues(version, newVersion);
            newVersion.setM_PriceList_ID(tarifas.get(version.getM_PriceList_ID()));
            newVersion.setM_DiscountSchema_ID(descuentos.get(version.getM_DiscountSchema_ID()));
            newVersion.setM_Pricelist_Version_Base_ID(tarifas.get(version.getM_Pricelist_Version_Base_ID()) == null ? 0 : tarifas.get(version.getM_Pricelist_Version_Base_ID()));
            newVersion.save();
        }
    }

    /**
	 * Copia los pay schedule del esquema de vencimiento template y le asigna el esquema nuevo 
	 * @param paymentTerm_template
	 * @param id_paymentTerm_new
	 * @throws Exception
	 */
    private void createPaySchedules(MPaymentTerm paymentTerm_template, int id_paymentTerm_new) throws Exception {
        MPaySchedule[] paySchedules = paymentTerm_template.getSchedule(true);
        this.setCtx(this.getNewClient());
        MPaySchedule newPaySchedule;
        for (MPaySchedule paySchedule : paySchedules) {
            newPaySchedule = new MPaySchedule(this.getCtx(), 0, this.getTrxName());
            MPaySchedule.copyValues(paySchedule, newPaySchedule);
            newPaySchedule.setC_PaymentTerm_ID(id_paymentTerm_new);
            newPaySchedule.save();
        }
    }

    /**
	 * Copia los esquemas de vencimiento de la compañía template
	 * @throws Exception
	 */
    private void createPaymentTerm() throws Exception {
        this.setCtx(this.getClientTemplate());
        List<MPaymentTerm> esquemas = MPaymentTerm.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MPaymentTerm newPaymentTerm;
        for (MPaymentTerm paymentTerm : esquemas) {
            newPaymentTerm = new MPaymentTerm(this.getCtx(), 0, this.getTrxName());
            MPaymentTerm.copyValues(paymentTerm, newPaymentTerm);
            newPaymentTerm.save();
            this.createPaySchedules(paymentTerm, newPaymentTerm.getID());
        }
    }

    private void createAccountingProcessor() throws Exception {
        this.setCtx(this.getClientTemplate());
        MAcctProcessor templateAcctProcessor = (MAcctProcessor) PO.findFirst(getCtx(), "C_AcctProcessor", "AD_Client_ID = ?", new Object[] { getClientTemplate() }, null, getTrxName());
        this.setCtx(this.getNewClient());
        MAcctProcessor newAcctProcessor = new MAcctProcessor(getCtx(), 0, getTrxName());
        PO.copyValues(templateAcctProcessor, newAcctProcessor);
        newAcctProcessor.setClientOrg(getNewClient(), 0);
        newAcctProcessor.setName("Servidor de Procesos Contable");
        newAcctProcessor.setDescription("Servidor de procesamiento contable");
        newAcctProcessor.setSupervisor_ID((users.get(templateAcctProcessor.getSupervisor_ID())).getAD_User_ID());
        newAcctProcessor.save();
    }

    /**
	 * Creo las categorías de uom a partir de la compañía template y lo devuelvo en un map para 
	 * asociar categorías nuevas con viejas
	 * @return
	 */
    private Map<Integer, Integer> createUOMCategories() {
        this.setCtx(this.getClientTemplate());
        List<MUOMGroup> categorias = MUOMGroup.getOfClient(this.getCtx(), this.getTrxName());
        Map<Integer, Integer> mapeo_categories = new HashMap<Integer, Integer>();
        this.setCtx(this.getNewClient());
        MUOMGroup newGroup;
        for (MUOMGroup group : categorias) {
            newGroup = new MUOMGroup(this.getCtx(), 0, this.getTrxName());
            MUOMGroup.copyValues(group, newGroup);
            newGroup.save();
            mapeo_categories.put(group.getID(), newGroup.getID());
        }
        return mapeo_categories;
    }

    /**
	 * Creo los uoms de la compañía template y retorno la última creada
	 * @return
	 */
    private int createUOMs() {
        this.setCtx(this.getClientTemplate());
        Map<Integer, Integer> categories = this.createUOMCategories();
        List<MUOM> uoms = MUOM.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MUOM newUom;
        Map<Integer, Integer> allUoms = new HashMap<Integer, Integer>();
        int uom_retorno = 0;
        for (MUOM muom : uoms) {
            newUom = new MUOM(this.getCtx(), 0, this.getTrxName());
            MUOM.copyValues(muom, newUom);
            newUom.setC_UOM_Group_ID(categories.get(muom.getC_UOM_Group_ID()));
            newUom.save();
            allUoms.put(muom.getID(), newUom.getID());
            uom_retorno = newUom.getID();
        }
        this.setCtx(this.getClientTemplate());
        List<MUOMConversion> conversiones = MUOMConversion.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        MUOMConversion newConversion;
        for (MUOMConversion conversion : conversiones) {
            newConversion = new MUOMConversion(this.getCtx(), 0, this.getTrxName());
            MUOMConversion.copyValues(conversion, newConversion);
            newConversion.setC_UOM_ID(allUoms.get(conversion.getC_UOM_ID()));
            newConversion.setC_UOM_To_ID(allUoms.get(conversion.getC_UOM_To_ID()));
            newConversion.setM_Product_ID(0);
            newConversion.save();
        }
        return uom_retorno;
    }

    /**
	 * Copio las locations de la compañía template
	 * @return
	 * @throws Exception
	 */
    private Map<Integer, Integer> createLocations() throws Exception {
        this.setCtx(this.getClientTemplate());
        List<MLocation> locations = MLocation.getOfClient(this.getCtx(), this.getTrxName());
        Map<Integer, Integer> allLocations = new HashMap<Integer, Integer>();
        MLocation newLocation;
        for (MLocation location : locations) {
            this.setCtx(this.getNewClient(), this.getOrgId(location.getAD_Org_ID()));
            newLocation = new MLocation(this.getCtx(), 0, this.getTrxName());
            MLocation.copyValues(location, newLocation);
            newLocation.save();
            allLocations.put(location.getID(), newLocation.getID());
        }
        return allLocations;
    }

    /**
	 * Crea las locators del warehouse pasado como parámetro
	 * @param warehouse_template
	 * @param new_warehouse_id
	 * @throws Exception
	 */
    private void createLocators(MWarehouse warehouse_template, int new_warehouse_id) throws Exception {
        MLocator[] locators = warehouse_template.getLocators(true);
        MLocator newLocator;
        for (int i = 0; i < locators.length; i++) {
            this.setCtx(this.getNewClient(), this.getOrgId(locators[i].getAD_Org_ID()));
            newLocator = new MLocator(this.getCtx(), 0, this.getTrxName());
            MLocator.copyValues(locators[i], newLocator);
            newLocator.setM_Warehouse_ID(new_warehouse_id);
            newLocator.save();
        }
    }

    /**
	 * Creo los almacenes y ubicaciones a partir de la compañía y la organización template
	 * @param ad_org_id
	 * @throws Exception
	 */
    private void createWarehousesAndLocators(Map<Integer, Integer> locations) throws Exception {
        locations = this.createLocations();
        this.setCtx(this.getClientTemplate());
        List<MWarehouse> almacenes = MWarehouse.getOfClient(this.getCtx(), this.getTrxName());
        MWarehouse newAlmacen;
        for (MWarehouse almacen : almacenes) {
            this.setCtx(this.getNewClient(), this.getOrgId(almacen.getAD_Org_ID()));
            newAlmacen = new MWarehouse(this.getCtx(), 0, this.getTrxName());
            MLocation.copyValues(almacen, newAlmacen);
            newAlmacen.setC_Location_ID(locations.get(almacen.getC_Location_ID()));
            newAlmacen.save();
            this.createLocators(almacen, newAlmacen.getID());
        }
    }

    /**
	 * Obtiene el grupo de las entidades comerciales si es que existe, sino la crea
	 * @return id del grupo
	 */
    private int createBPGroup() throws Exception {
        int idBGP = Env.getContextAsInt(this.getCtx(), "#C_BP_Group_ID");
        if (idBGP == 0) {
            this.setCtx(this.getNewClient(), 0);
            MBPGroup bpg = new MBPGroup(this.getCtx(), 0, this.getTrxName());
            idBGP = bpg.getID();
            bpg.setValue("Empleados");
            bpg.setName("Empleados");
            if (bpg.save()) {
                idBGP = bpg.getID();
                this.getLog().info("BPartnerGroup creado con id " + idBGP);
            } else {
                throw new Exception("No se pudo crear el grupo de la entidad comercial");
            }
        }
        return idBGP;
    }

    /**
	 * Crea las organizaciones para la compañía a partir de la compañía template
	 */
    private void createOrgs() throws Exception {
        this.setCtx(this.getClientTemplate());
        MOrg[] orgs = MOrg.getOfClient(this.getCtx(), this.getTrxName());
        this.setCtx(this.getNewClient());
        int total = orgs.length;
        MOrg org = null;
        MOrg orgAux = null;
        for (int i = 0; i < total; i++) {
            orgAux = orgs[i];
            org = new MOrg(ctx, 0, this.getTrxName());
            org.setValue(orgAux.getName());
            org.setName(orgAux.getName());
            if (org.save()) {
                this.getLog().info("Organizacion copiada de la template. Nombre de la nueva org: " + org.getName());
                this.getOrgs().put(orgAux.getID(), org);
            } else {
                throw new Exception("No se pudieron crear las organizaciones");
            }
        }
    }

    /**
	 * Elimina los formatos de impresión del botón imprimir para poder agregarlos nuevos, si es que hay algunos
	 * @throws Exception
	 */
    private void delPrintForms() throws Exception {
        String sql = "DELETE FROM ad_printform WHERE ad_client_id = ?";
        ExecuterSql.executeUpdate(sql, this.getTrxName(), new Object[] { this.getClientTemplate() });
    }

    /**
	 * Copia los formatos de impresión del botón Imprimir de System
	 */
    private void createPrintForm() throws Exception {
        this.setCtx(0);
        List<MPrintForm> list = MPrintForm.getOfClient(this.getCtx(), this.getTrxName());
        this.delPrintForms();
        this.setCtx(this.getNewClient());
        MPrintForm newPrintForm;
        for (MPrintForm printForm : list) {
            this.setCtx(this.getNewClient(), this.getOrgId(printForm.getAD_Org_ID()));
            newPrintForm = new MPrintForm(this.getCtx(), 0, this.getTrxName());
            MPrintForm.copyValues(printForm, newPrintForm);
            newPrintForm.setIsActive(printForm.isActive());
            if (!newPrintForm.save()) {
                throw new Exception("No se pudieron crear los formatos de impresion del boton Imprimir");
            }
        }
    }

    /**
	 * Creo la location para BPartner 
	 * @param C_Location_ID id de la location template
	 * @return el id de la nueva location
	 */
    private int createBPLocation(int C_Location_ID) throws Exception {
        MLocation location = new MLocation(this.getCtx(), C_Location_ID, this.getTrxName());
        this.setCtx(this.getNewClient());
        MLocation newLocation = new MLocation(this.getCtx(), 0, this.getTrxName());
        newLocation.setAddress1(location.getAddress1());
        newLocation.setCity(location.getCity());
        newLocation.setC_Country_ID(location.getC_Country_ID());
        newLocation.setC_Region_ID(location.getC_Region_ID());
        if (!newLocation.save()) {
            throw new Exception("No se pudo crear la localización para el usuario");
        }
        int id = newLocation.getID();
        return id;
    }

    /**
	 * Crea el BPartner para el usuario a agregar
	 * @param C_BPartner_Group_ID grupo del bpartner
	 * @param name value y nombre del bpartner
	 * @return el id del bpartner creado
	 */
    private int createBPartner(int C_BPartner_Group_ID, int C_BPartner_ID, String name) throws Exception {
        MBPartner bpTemplate = new MBPartner(this.getCtx(), C_BPartner_ID, this.getTrxName());
        MClient client = new MClient(this.getCtx(), this.getNewClient(), this.getTrxName());
        int location = this.createBPLocation(bpTemplate.getC_Location_ID());
        MBPartner bp = new MBPartner(this.getCtx(), 0, this.getTrxName());
        bp.setValue(client.getName() + " " + name);
        bp.setName(client.getName() + " " + name);
        bp.setC_BP_Group_ID(C_BPartner_Group_ID);
        bp.setIsEmployee(true);
        bp.setIsSalesRep(true);
        bp.setC_Location_ID(location);
        int id = 0;
        if (bp.save()) {
            this.getLog().info("BPartner creado con nombre " + bp.getName());
            id = bp.getID();
        } else {
            throw new Exception("No se pudo crear la entidad comercial para el usuario");
        }
        return id;
    }

    /**
	 * Crea un usuario a partir del usuario template
	 * @param userTemplate usuario template
	 * @param bpGroup id del grupo de BPartner
	 * @return id del usuario creado
	 */
    private int createUser(MUser userTemplate, int bpGroup) throws Exception {
        int idBp = this.createBPartner(bpGroup, userTemplate.getC_BPartner_ID(), userTemplate.getName());
        this.setCtx(this.getNewClient(), this.getOrgId(userTemplate.getAD_Org_ID()));
        MClient client = new MClient(this.getCtx(), this.getNewClient(), this.getTrxName());
        MUser user = new MUser(this.getCtx(), 0, this.getTrxName());
        String companyName = client.getName().replaceAll(" ", "");
        String userName = userTemplate.getName().replaceAll("WideFast-Track", companyName);
        user.setName(userName);
        user.setDescription(userTemplate.getDescription());
        user.setPassword(userName);
        user.setC_BPartner_ID(idBp);
        int idUser = 0;
        if (user.save()) {
            this.getLog().info("Usuario creado con nombre: " + user.getName());
            idUser = user.getID();
        } else {
            throw new Exception("No se pudo crear el usuario a partir del template");
        }
        users.put(userTemplate.getAD_User_ID(), user);
        return idUser;
    }

    /**
	 * Crea el nuevo tree a partir del tree template
	 * @param AD_Tree_ID tree template
	 * @return id del nuevo tree
	 */
    private int createTree(int AD_Tree_ID) throws Exception {
        this.setCtx(this.getClientTemplate());
        MTree treeAux = new MTree(this.getCtx(), AD_Tree_ID, this.getTrxName());
        this.setCtx(this.getNewClient(), 0);
        MTree newTree = new MTree(this.getCtx(), 0, this.getTrxName());
        newTree.setName(treeAux.getName());
        newTree.setTreeType(treeAux.getTreeType());
        newTree.setIsAllNodes(false);
        newTree.setProcessing(false);
        if (!newTree.save()) {
            throw new Exception("No se pudo crear el arbol de menu");
        }
        return newTree.getID();
    }

    /**
	 * Crea los accesos a ventanas
	 */
    private void createWindowsAccess_old() {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        String sql = null;
        PreparedStatement psmt = null;
        MWindowAccess mwa = null;
        ResultSet rs = null;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                sql = "SELECT * FROM ad_window_access WHERE ad_role_id = ? AND ad_client_id = ?";
                psmt = DB.prepareStatement(sql, this.getTrxName());
                psmt.setInt(1, key.intValue());
                psmt.setInt(2, this.getClientTemplate());
                rs = psmt.executeQuery();
                while (rs.next()) {
                    mwa = new MWindowAccess(this.getCtx(), 0, this.getTrxName());
                    mwa.setAD_Role_ID(((MRole) this.getRoles().get(key)).getID());
                    mwa.setAD_Window_ID(rs.getInt("ad_window_id"));
                    if (mwa.save()) {
                    }
                }
            }
        } catch (Exception e) {
            this.getLog().log(Level.SEVERE, sql, e);
        }
    }

    /**
	 * Crea los accesos a procesos
	 */
    private void createProcessAccess() {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        String sql = null;
        PreparedStatement psmt = null;
        MProcessAccess mpa = null;
        ResultSet rs = null;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                sql = "SELECT * FROM ad_process_access WHERE ad_role_id = ? AND ad_client_id = ?";
                psmt = DB.prepareStatement(sql, this.getTrxName());
                psmt.setInt(1, key.intValue());
                psmt.setInt(2, this.getClientTemplate());
                rs = psmt.executeQuery();
                while (rs.next()) {
                    mpa = new MProcessAccess(this.getCtx(), 0, this.getTrxName());
                    mpa.setAD_Role_ID(((MRole) this.getRoles().get(key)).getID());
                    mpa.setAD_Process_ID(rs.getInt("ad_process_id"));
                    if (mpa.save()) {
                    }
                }
            }
        } catch (Exception e) {
            this.getLog().log(Level.SEVERE, sql, e);
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }

    /**
	 * Crea los accesos de forms
	 */
    private void createFormAccess() {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        String sql = null;
        PreparedStatement psmt = null;
        MFormAccess mfa = null;
        ResultSet rs = null;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                sql = "SELECT * FROM ad_form_access WHERE ad_role_id = ? AND ad_client_id = ?";
                psmt = DB.prepareStatement(sql, this.getTrxName());
                psmt.setInt(1, key.intValue());
                psmt.setInt(2, this.getClientTemplate());
                rs = psmt.executeQuery();
                while (rs.next()) {
                    mfa = new MFormAccess(this.getCtx(), 0, this.getTrxName());
                    mfa.setAD_Role_ID(((MRole) this.getRoles().get(key)).getID());
                    mfa.setAD_Form_ID(rs.getInt("ad_form_id"));
                    if (mfa.save()) {
                    }
                }
            }
        } catch (Exception e) {
            this.getLog().log(Level.SEVERE, sql, e);
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }

    /**
	 * Crea los accesos de los workflows
	 */
    private void createWorkflowAccess() {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        String sql = null;
        PreparedStatement psmt = null;
        MWorkflowAccess mwa = null;
        ResultSet rs = null;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                sql = "SELECT * FROM ad_workflow_access WHERE ad_role_id = ? AND ad_client_id = ?";
                psmt = DB.prepareStatement(sql, this.getTrxName());
                psmt.setInt(1, key.intValue());
                psmt.setInt(2, this.getClientTemplate());
                rs = psmt.executeQuery();
                while (rs.next()) {
                    mwa = new MWorkflowAccess(this.getCtx(), 0, this.getTrxName());
                    mwa.setAD_Role_ID(((MRole) this.getRoles().get(key)).getID());
                    mwa.setAD_Workflow_ID(rs.getInt("ad_workflow_id"));
                    if (mwa.save()) {
                    }
                }
            }
        } catch (Exception e) {
            this.getLog().log(Level.SEVERE, sql, e);
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }

    /**
	 * Copia los accessos a las pestañas de los roles
	 * @throws Exception
	 */
    private void createTabAccess() throws Exception {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        MRole valueRol;
        PreparedStatement psmt = null;
        ResultSet rs = null;
        String sql;
        int nro;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                valueRol = (MRole) this.getRoles().get(key);
                List<MTabAccess> tabRoles = MTabAccess.getOfRoleInList(key.intValue(), this.getTrxName());
                for (MTabAccess auxMta : tabRoles) {
                    sql = "INSERT INTO ad_tab_access (ad_tab_access_id,ad_tab_id,ad_window_id,ad_role_id,ad_client_id,ad_org_id,isactive,createdby,updatedby,isreadwrite) VALUES (?,?,?,?,?,?,?,?,?,?)";
                    nro = MSequence.getNextID(this.getNewClient(), "AD_Tab_Access", this.getTrxName());
                    psmt = DB.prepareStatement(sql, this.getTrxName());
                    psmt.setInt(1, nro);
                    psmt.setInt(2, auxMta.getAD_Tab_ID());
                    psmt.setInt(3, auxMta.getAD_Window_ID());
                    psmt.setInt(4, valueRol.getID());
                    psmt.setInt(5, this.getNewClient());
                    psmt.setInt(6, 0);
                    psmt.setString(7, (auxMta.isActive()) ? "Y" : "N");
                    psmt.setInt(8, 100);
                    psmt.setInt(9, 100);
                    psmt.setString(10, (auxMta.isReadWrite()) ? "Y" : "N");
                    psmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }

    /**
	 * Copia los accesos a las pestañas de cada uno de los perfiles (OLD)
	 * @throws Exception
	 */
    private void createTabAccess_old() throws Exception {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        MRole valueRol;
        PreparedStatement psmt = null;
        MTabAccess mta = null;
        ResultSet rs = null;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                valueRol = (MRole) this.getRoles().get(key);
                List<MTabAccess> tabRoles = MTabAccess.getOfRoleInList(key.intValue(), this.getTrxName());
                for (MTabAccess auxMta : tabRoles) {
                    mta = new MTabAccess(this.getCtx(), 0, this.getTrxName());
                    mta.setAD_Role_ID(valueRol.getID());
                    mta.setAD_Tab_ID(auxMta.getAD_Tab_ID());
                    mta.setAD_Window_ID(auxMta.getAD_Window_ID());
                    mta.setIsReadWrite(auxMta.isReadWrite());
                    if (!mta.save()) {
                        throw new Exception("No se pudieron crear los accesos a las pestañas");
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }

    /**
	 * Crea los accesos a las ventanas en base a las ventanas del menú
	 */
    private void createWindowsAccess() throws Exception {
        this.setCtx(this.getNewClient(), 0);
        Set conjRoles = this.getRoles().keySet();
        Iterator iteraKeys = conjRoles.iterator();
        Integer key = null;
        MRole valueRol;
        String sql = null;
        PreparedStatement psmt = null;
        MWindowAccess mwa = null;
        MWindowAccess aux = null;
        ResultSet rs = null;
        try {
            while (iteraKeys.hasNext()) {
                key = (Integer) iteraKeys.next();
                valueRol = (MRole) this.getRoles().get(key);
                if (valueRol.getAD_Tree_Menu_ID() != 0) {
                    sql = "SELECT ad_window_id FROM ad_treenodemm as tn	INNER JOIN ad_role as r	ON (ad_role_id = ?) AND (tn.ad_tree_id = r.ad_tree_menu_id)	INNER JOIN ad_menu as m	ON (m.ad_menu_id = node_id) WHERE ACTION = 'W'";
                    psmt = DB.prepareStatement(sql, this.getTrxName());
                    psmt.setInt(1, valueRol.getID());
                    rs = psmt.executeQuery();
                    while (rs.next()) {
                        mwa = MWindowAccess.getOfRoleAndWindow(this.getCtx(), valueRol.getID(), rs.getInt("ad_window_id"), this.getTrxName());
                        aux = MWindowAccess.getOfRoleAndWindow(this.getCtx(), key.intValue(), rs.getInt("ad_window_id"), this.getTrxName());
                        if (mwa == null) {
                            mwa = new MWindowAccess(this.getCtx(), 0, this.getTrxName());
                            mwa.setAD_Window_ID(rs.getInt("ad_window_id"));
                            mwa.setAD_Role_ID(valueRol.getID());
                        }
                        mwa.setIsActive((aux != null) ? aux.isActive() : true);
                        mwa.setIsReadWrite((aux != null) ? aux.isReadWrite() : true);
                        if (!mwa.save()) {
                            throw new Exception("No se pudieron crear los accesos para las ventanas");
                        }
                    }
                }
                sql = "DELETE FROM ad_window_access WHERE (ad_window_id IN (SELECT ad_window_id FROM ad_window_access WHERE (ad_role_id = ?) EXCEPT SELECT ad_window_id FROM ad_treenodemm as tn INNER JOIN ad_role as r ON (ad_role_id = ?) AND (tn.ad_tree_id = r.ad_tree_menu_id) INNER JOIN ad_menu as m ON (m.ad_menu_id = node_id) WHERE ACTION = 'W')) and (ad_role_id = ?)";
                psmt = DB.prepareStatement(sql, this.getTrxName());
                psmt.setInt(1, valueRol.getID());
                psmt.setInt(2, valueRol.getID());
                psmt.setInt(3, valueRol.getID());
                psmt.executeUpdate();
            }
        } catch (Exception e) {
            this.getLog().log(Level.SEVERE, sql, e);
            throw new Exception(e.getMessage());
        } finally {
            try {
                rs.close();
                psmt.close();
                psmt = null;
            } catch (Exception e) {
                psmt = null;
            }
        }
    }

    /**
	 * Crea los accesos a componentes a partir de los roles template 
	 */
    private void createMenuAccess() throws Exception {
        this.createWindowsAccess();
        this.createTabAccess();
    }

    /**
	 * Creo el árbol de menú nuevo con todos sus nodos para el nuevo perfil a partir del árbol template
	 * @param AD_Tree_ID árbol template
	 * @return id del árbol creado
	 */
    private int createTreeMenu(int AD_Tree_ID) throws Exception {
        int treeRetorno = 0;
        if (AD_Tree_ID != 0) {
            this.setCtx(this.getClientTemplate());
            MTree_NodeMM[] nodosMenu = MTree_NodeMM.getOfTree(this.getCtx(), AD_Tree_ID, this.getTrxName());
            int total = nodosMenu.length;
            treeRetorno = this.createTree(AD_Tree_ID);
            this.getTrees().put(AD_Tree_ID, treeRetorno);
            this.setCtx(this.getNewClient(), 0);
            MTree_NodeMM treeNodeMMAux = null;
            MTree_NodeMM treeNodeMM = null;
            for (int i = 0; i < total; i++) {
                treeNodeMMAux = nodosMenu[i];
                if (treeNodeMMAux.getNode_ID() != 0) {
                    treeNodeMM = new MTree_NodeMM(this.getCtx(), 0, this.getTrxName());
                    treeNodeMM.setAD_Tree_ID(treeRetorno);
                    treeNodeMM.setNode_ID(treeNodeMMAux.getNode_ID());
                    treeNodeMM.setParent_ID(treeNodeMMAux.getParent_ID());
                    treeNodeMM.setSeqNo(treeNodeMMAux.getSeqNo());
                    if (!treeNodeMM.save()) {
                        throw new Exception("No se pudieron crear los nodos para el arbol de menu");
                    }
                }
            }
        }
        return treeRetorno;
    }

    /**
	 * Crea los perfiles a partir de los perfiles template de la compañía template
	 */
    private void createRoles() throws Exception {
        this.setCtx(this.getClientTemplate());
        MRole[] roles = MRole.getOfClient(this.getCtx());
        this.setCtx(this.getNewClient(), 0);
        int total = roles.length;
        MRole roleAux = null;
        MRole newRole = null;
        for (int i = 0; i < total; i++) {
            roleAux = roles[i];
            newRole = new MRole(this.getCtx(), 0, this.getTrxName());
            newRole.setName(roleAux.getName());
            newRole.setIsActive(roleAux.isActive());
            newRole.setUserLevel(roleAux.getUserLevel());
            newRole.setIsShowAcct(roleAux.isShowAcct());
            newRole.setPreferenceType(roleAux.getPreferenceType());
            newRole.setAD_Tree_Menu_ID(this.createTreeMenu(roleAux.getAD_Tree_Menu_ID()));
            if (!newRole.save()) {
                throw new Exception("No se pudo crear los perfiles");
            }
            this.getRoles().put(roleAux.getID(), newRole);
        }
    }

    /**
	 * Ddetermina los roles de usuario dependiendo que tipo de usuario sea
	 * @param idUserTemplate usuario template
	 * @return array de roles de ese usuario template
	 */
    private MUserRoles[] getUserRoles(int idUserTemplate) {
        MUserRoles[] retorno = null;
        if (idUserTemplate != 100) {
            retorno = MUserRoles.getOfUser(this.getCtx(), idUserTemplate);
        } else {
            retorno = MUserRoles.getOfUserAndClient(this.getCtx(), idUserTemplate, this.getClientTemplate(), this.getTrxName());
        }
        return retorno;
    }

    /**
	 * Crea los perfiles de los usuarios  
	 * @param idUserTemplate id de usuario template
	 * @param idUser id de usuario nuevo
	 */
    private void createUserProfiles(int idUserTemplate, int idUser) throws Exception {
        this.setCtx(this.getClientTemplate());
        MUserRoles[] userRoles = this.getUserRoles(idUserTemplate);
        int total = userRoles.length;
        MUserRoles userRole = null;
        MUserRoles userRoleAux = null;
        for (int i = 0; i < total; i++) {
            userRoleAux = userRoles[i];
            this.setCtx(this.getNewClient(), this.getOrgId(userRoleAux.getAD_Org_ID()));
            userRole = new MUserRoles(this.getCtx(), 0, this.getTrxName());
            userRole.setAD_Role_ID(((MRole) this.getRoles().get(userRoleAux.getAD_Role_ID())).getID());
            userRole.setAD_User_ID(idUser);
            userRole.setIsActive(userRoleAux.isActive());
            if (!userRole.save()) {
                throw new Exception("No se pudo crear los roles del usuario");
            }
        }
    }

    /**
	 * Crea los accesos a las organizaciones de los perfiles a partir de la compañía template
	 */
    private void createRoleOrgAccess() throws Exception {
        MRoleOrgAccess[] orgRoles = MRoleOrgAccess.getOfClient(this.getCtx(), this.getClientTemplate());
        int total = orgRoles.length;
        MRoleOrgAccess orgRole = null;
        MRoleOrgAccess orgRoleAux = null;
        for (int i = 0; i < total; i++) {
            orgRoleAux = orgRoles[i];
            this.setCtx(this.getNewClient(), this.getOrgId(orgRoleAux.getAD_Org_ID()));
            orgRole = new MRoleOrgAccess(this.getCtx(), 0, this.getTrxName());
            orgRole.setAD_Role_ID(((MRole) this.getRoles().get(orgRoleAux.getAD_Role_ID())).getAD_Role_ID());
            if (!orgRole.save()) {
                throw new Exception("No se pudo crear los roles para las organizaciones");
            }
        }
    }

    /**
	 * Crea los usuarios a partir de los usuarios de la compañía template
	 */
    private void createAllUsers() throws Exception {
        int bpGroup = this.createBPGroup();
        this.setCtx(this.getClientTemplate());
        MUser[] users = MUser.getOfClient(this.getCtx(), this.getTrxName());
        int total = users.length;
        MUser userAux = null;
        for (int i = 0; i < total; i++) {
            userAux = users[i];
            int idUser = this.createUser(userAux, bpGroup);
            this.createUserProfiles(userAux.getID(), idUser);
            for (X_AD_TreeBar treeBarEntry : getTreeBarEntries()) {
                if (treeBarEntry.getAD_User_ID() == userAux.getAD_User_ID()) {
                    copyTreeBarEntry(treeBarEntry, idUser);
                }
            }
        }
        for (X_AD_TreeBar treeBarEntry : getTreeBarEntries()) if (treeBarEntry.getAD_User_ID() == USER_SUPERVISOR_ID) copyTreeBarEntry(treeBarEntry, USER_SUPERVISOR_ID);
    }

    /**
	 * Genera una copia de una instancia de X_AD_TreeBar segun la informacion de los HashMaps existentes  
	 * @param sourceTreeBar entrada original a copiar
	 * @param newUserID nuevo usuario a asignar la entrada
	 */
    private void copyTreeBarEntry(X_AD_TreeBar sourceTreeBar, int newUserID) {
        MTreeBar newTreeBar = new MTreeBar(getCtx(), 0, getTrxName());
        newTreeBar.setAD_Tree_ID(getTrees().get(sourceTreeBar.getAD_Tree_ID()));
        newTreeBar.setAD_User_ID(newUserID);
        newTreeBar.setNode_ID(sourceTreeBar.getNode_ID());
        newTreeBar.setClientOrg(getNewClient(), ((MOrg) (getOrgs().get(sourceTreeBar.getAD_Org_ID()))).getAD_Org_ID());
        newTreeBar.setIsActive(true);
        newTreeBar.save();
    }

    /**
	 * Copia toda la información de la compañía template a la nueva compañía
	 */
    public void ejecutar() throws Exception {
        this.createInitialInfo();
        this.createAllUsers();
        this.configureClient();
    }

    public void deshacer() {
    }
}
