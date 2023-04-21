package biz.wavelet.thickclient.data.db;

import biz.wavelet.thickclient.util.TimeFormat;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import thickclient.sync.ItemObject;

public class ItemDAO extends BaseDAO {

    public static final String PKID = "pkid";

    public static final String ITEM_CODE = "item_code";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String STATUS = "status";

    public static final String LASTUPDATE = "lastupdate";

    public static final String USERID_EDIT = "userid_edit";

    public static final String UOM = "uom";

    public static final String UNIT_OF_MEASURE = "unit_of_meas";

    public static final String INV_TYPE = "inv_type";

    public static final String ITEM_TYPE1 = "item_type1";

    public static final String ITEM_TYPE2 = "item_type2";

    public static final String ITEM_TYPE3 = "item_type3";

    public static final String GLCODE = "glcode";

    public static final String CATEGORYID = "categoryid";

    public static final String STATE = "state";

    public static final String CATEGORY1 = "category1";

    public static final String CATEGORY2 = "category2";

    public static final String CATEGORY3 = "category3";

    public static final String CATEGORY4 = "category4";

    public static final String CATEGORY5 = "category5";

    public static final String HAS_CHILDREN = "has_children";

    public static final String PARENTID = "parentid";

    public static final String PARENTRATIO = "parent_ratio";

    public static final String EANCODE = "ean_code";

    public static final String UPCCODE = "upc_code";

    public static final String ISBNCODE = "isbn_code";

    public static final String PRICELIST = "price_list";

    public static final String PRICESALE = "price_sale";

    public static final String PRICEDISC1 = "price_disc1";

    public static final String PRICEDISC2 = "price_disc2";

    public static final String PRICEDISC3 = "price_disc3";

    public static final String PRICEMIN = "price_min";

    public static final String FIFOUNITCOST = "fifo_unit_cost";

    public static final String MAUNITCOST = "ma_unit_cost";

    public static final String WAUNITCOST = "wa_unit_cost";

    public static final String LASTUNITCOST = "last_unit_cost";

    public static final String REPLACEMENTUNITCOST = "replacement_cost";

    public static final String SERIALIZED = "serialized";

    public static final String WEIGHT = "weight";

    public static final String LENGTH = "length";

    public static final String WIDTH = "width";

    public static final String DEPTH = "depth";

    public static final String PREFERREDSUPPLIER = "preferred_supplier";

    public static final String MINORDERQTY = "min_order_qty";

    public static final String LEADTIME = "lead_time";

    public static final String RESERVED1 = "reserved1";

    public static final String RESERVED2 = "reserved2";

    public static final String RESERVED3 = "reserved3";

    public static final String PIX_THUMB_NAME = "pix_thumb_name";

    public static final String PIX_FULL_NAME = "pix_full_name";

    public static final String PIX_THUMBNAIL = "pix_thumbnail";

    public static final String PIX_FULLSIZE = "pix_fullsize";

    public static final String REBATE1_PCT = "rebate1_pct";

    public static final String REBATE1_PRICE = "rebate1_price";

    public static final String REBATE1_START = "rebate1_start";

    public static final String REBATE1_END = "rebate1_end";

    public static final String DISC1_PCT = "disc1_pct";

    public static final String DISC1_AMOUNT = "disc1_amount";

    public static final String DISC1_START = "disc1_start";

    public static final String DISC1_END = "disc1_end";

    public static final String PRODUCTION_REQUIRED = "production_required";

    public static final String PRODUCTION_LEAD_TIME = "production_lead_time";

    public static final String PRODUCTION_COST = "production_cost";

    public static final String PRODUCTION_PROCESS = "production_process";

    public static final String PRODUCTION_NAME = "production_name";

    public static final String PRODUCTION_DESCRIPTION = "production_description";

    public static final String DELIVERY_REQUIRED = "delivery_required";

    public static final String DELIVERY_LEAD_TIME = "delivery_lead_time";

    public static final String DELIVERY_COST = "delivery_cost";

    public static final String DELIVERY_PROCESS = "delivery_process";

    public static final String DELIVERY_NAME = "delivery_name";

    public static final String DELIVERY_DESCRIPTION = "delivery_description";

    public static final String DELTA_PRICE_RETAIL_AMT = "delta_price_retail_amt";

    public static final String DELTA_PRICE_RETAIL_PCT = "delta_price_retail_pct";

    public static final String DELTA_PRICE_DEALER_AMT = "delta_price_dealer_amt";

    public static final String DELTA_PRICE_DEALER_PCT = "delta_price_dealer_pct";

    public static final String DELTA_PRICE_OUTLET_AMT = "delta_price_outlet_amt";

    public static final String DELTA_PRICE_OUTLET_PCT = "delta_price_outlet_pct";

    public static final String DISC_MAX_PCT = "disc_max_pct";

    public static final String WARRANTY_TYPE = "warranty_type";

    public static final String WARRANTY_PERIOD = "warranty_period";

    public static final String SHELF_LIFE = "shelf_life";

    public static final String COMMISSION_PCT_SALES1 = "commission_pct_sales1";

    public static final String COMMISSION_PCT_SALES2 = "commission_pct_sales2";

    public static final String COMMISSION_PCT_SALES3 = "commission_pct_sales3";

    public static final String COMMISSION_PCT_SALES4 = "commission_pct_sales4";

    public static final String COMMISSION_PCT_PARTNER = "commission_pct_partner";

    public static final String COMMISSION_PCT_GP = "commission_pct_gp";

    public static final String LOGIC_REORDER = "logic_reorder";

    public static final String LOGIC_MAX_QTY = "logic_max_qty";

    public static final String THRESHOLD_QTY_REORDER = "threshold_qty_reorder";

    public static final String THRESHOLD_QTY_MAX_QTY = "threshold_qty_max_qty";

    public static final String REMARKS1 = "remarks1";

    public static final String REMARKS2 = "remarks2";

    public static final String KEYWORDS = "keywords";

    public static final String PREFIX_LOGIC = "prefix_logic";

    public static final String PREFIX_LENGTH = "prefix_length";

    public static final String PREFIX_CODE = "prefix_code";

    public static final String POSTFIX_LOGIC = "postfix_logic";

    public static final String POSTFIX_LENGTH = "postfix_length";

    public static final String POSTFIX_CODE = "postfix_code";

    public static final String CODE_PROJECT = "code_project";

    public static final String CODE_DEPARTMENT = "code_department";

    public static final String CODE_DEALER = "code_dealer";

    public static final String CODE_SALESMAN = "code_salesman";

    public static final String TAX_OPTION = "tax_option";

    public static final String TAX_RATE = "tax_rate";

    public static final String PRICEECOM = "price_ecom";

    public static final String OUT_QTY = "out_qty";

    public static final String OUT_UNIT = "out_unit";

    public static final String IN_QTY = "in_qty";

    public static final String IN_UNIT = "in_unit";

    public static final String INN_QTY = "inn_qty";

    public static final String INN_UNIT = "inn_unit";

    public static final String INM_QTY = "inm_qty";

    public static final String INM_UNIT = "inm_unit";

    public static final Integer PKID_DEFAULT = new Integer("-1");

    public static final Integer PKID_DELIVERY = new Integer("200");

    public static final String STATUS_ACTIVE = "active";

    public static final String STATUS_INACTIVE = "inactive";

    public static final String STATUS_DELETED = "deleted";

    public static final String PRICING_RETAIL = "RETAIL";

    public static final String PRICING_WHOLESALE = "WHOLESALE";

    public static final String PRICING_DISC1 = "DISC1";

    public static final String PRICING_DISC2 = "DISC2";

    public static final String PRICING_DISC3 = "DISC3";

    public static final String CODE_NON_INV = "NON-INV";

    public static final String CODE_PREFIX_NS = "NS-";

    public static final String CODE_PREFIX_TI = "TI";

    public static final int INV_TYPE_NONE = 0;

    public static final int INV_TYPE_INVENTORY = 1;

    public static final String CODE_DELIVERY = "DELIVERY";

    public static final String CODE_GST = "GST";

    public static final String CODE_VAT = "VAT";

    public static final int INV_TYPE_NONSTK = 2;

    public static final int INV_TYPE_TRADEIN = 3;

    public static final String ITEM_TYPE_INVENTORY = "inv";

    public static final String ITEM_TYPE_NON_INVENTORY = "noninv";

    public static final String ITEM_TYPE_SERVICE = "svc";

    public static final String ITEM_TYPE_RAW_MATERIAL = "raw";

    public static final String ITEM_TYPE_SEMI_FINISHED = "semi";

    public static final String ITEM_TYPE_FINISHED_GOODS = "fin";

    public static final String ITEM_TYPE_PARTS = "parts";

    public static final String ITEM_TYPE_PACKAGE = "pkg";

    public static final String ITEM_TYPE_CONSIGN = "consign";

    public static final String ITEM_TYPE_ITEM = "item";

    public static final String RESERVED1_DEFAULT = "";

    public static final String RESERVED1_VALENTINE = "VALENTINE";

    public static final String RESERVED1_NON_VALENTINE = "NON-VALENTINE";

    public static final String RESERVED2_NOT_ENTITLED = "";

    public static final String RESERVED2_ENTITLED = "PROMO-ENTITLED";

    public static final String TAX_ENABLED = "ENABLED";

    public static final int UOM_NONE = 0;

    public static final int UOM_PCS = 1;

    public static final int UOM_SET = 2;

    public static final int UOM_UNIT = 3;

    public static final int UOM_ROLL = 4;

    public static final int DEF_CATID = 1000;

    public static final String TABLENAME = "INV_ITEM";

    private static boolean checkedTable = false;

    public ItemDAO() {
        log = Logger.getLogger(ItemDAO.class.getName());
        checkTable();
    }

    private void checkTable() {
    }

    public void insertObject(ItemObject valObj) {
        Connection conn = null;
        checkTable();
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            String insertStatement = "insert into " + TABLENAME + " values ( " + valObj.getPkid() + ",'" + valObj.getCode().replaceAll("'", "''") + "','" + valObj.getName().replaceAll("'", "''") + "','" + valObj.getDescription().replaceAll("'", "''") + "','" + valObj.getStatus().replaceAll("'", "''") + "','" + new Timestamp(valObj.getLastUpdate()).toString() + "'," + valObj.getUserIdUpdate() + ",'" + valObj.getUom().replaceAll("'", "''") + "'," + valObj.getUnitOfMeasure() + "," + valObj.getEnumInvType() + ",'" + valObj.getItemType1().replaceAll("'", "''") + "','" + valObj.getItemType2().replaceAll("'", "''") + "','" + valObj.getItemType3().replaceAll("'", "''") + "','" + valObj.getGlcode().replaceAll("'", "''") + "','" + valObj.getState().replaceAll("'", "''") + "'," + valObj.getCategoryId() + ",'" + valObj.getCategory1().replaceAll("'", "''") + "','" + valObj.getCategory2().replaceAll("'", "''") + "','" + valObj.getCategory3().replaceAll("'", "''") + "','" + valObj.getCategory4().replaceAll("'", "''") + "','" + valObj.getCategory5().replaceAll("'", "''") + "'," + (int) (valObj.isHasChildren() ? 1 : 0) + "," + valObj.getParentId() + "," + valObj.getParentRatio() + ",'" + valObj.getEanCode().replaceAll("'", "''") + "','" + valObj.getUpcCode().replaceAll("'", "''") + "','" + valObj.getIsbnCode().replaceAll("'", "''") + "'," + valObj.getPriceList() + "," + valObj.getPriceSale() + "," + valObj.getPriceDisc1() + "," + valObj.getPriceDisc2() + "," + valObj.getPriceDisc3() + "," + valObj.getPriceMin() + "," + valObj.getFifoUnitCost() + "," + valObj.getMaUnitCost() + "," + valObj.getWaUnitCost() + "," + valObj.getLastUnitCost() + "," + valObj.getReplacementUnitCost() + "," + (int) (valObj.isSerialized() ? 1 : 0) + "," + valObj.getWeight() + "," + valObj.getLength() + "," + valObj.getWidth() + "," + valObj.getDepth() + "," + valObj.getPreferredSupplier() + "," + valObj.getMinOrderQty() + "," + valObj.getLeadTime() + ",'" + valObj.getReserved1().replaceAll("'", "''") + "','" + valObj.getReserved2().replaceAll("'", "''") + "','" + valObj.getReserved3().replaceAll("'", "''") + "','" + valObj.getPixThumbName().replaceAll("'", "''") + "','" + valObj.getPixFullName().replaceAll("'", "''") + "','" + "" + "','" + "" + "'," + valObj.getRebate1Pct() + "," + valObj.getRebate1Price() + ",'" + new Timestamp(valObj.getRebate1Start()).toString() + "','" + new Timestamp(valObj.getRebate1End()).toString() + "'," + valObj.getDisc1Pct() + "," + valObj.getDisc1Amount() + ",'" + new Timestamp(valObj.getDisc1Start()).toString() + "','" + new Timestamp(valObj.getDisc1End()).toString() + "'," + (int) (valObj.isProductionRequired() ? 1 : 0) + "," + valObj.getProductionLeadTime() + "," + valObj.getProductionCost() + ",'" + valObj.getProductionProcess().replaceAll("'", "''") + "','" + valObj.getProductionName().replaceAll("'", "''") + "','" + valObj.getProductionDescription().replaceAll("'", "''") + "'," + (int) (valObj.isDeliveryRequired() ? 1 : 0) + "," + valObj.getDeliveryLeadTime() + "," + valObj.getDeliveryCost() + ",'" + valObj.getDeliveryProcess().replaceAll("'", "''") + "','" + valObj.getDeliveryName().replaceAll("'", "''") + "','" + valObj.getDeliveryDescription().replaceAll("'", "''") + "'," + valObj.getDeltaPriceRetailAmt() + "," + valObj.getDeltaPriceRetailPct() + "," + valObj.getDeltaPriceDealerAmt() + "," + valObj.getDeltaPriceDealerPct() + "," + valObj.getDeltaPriceOutletAmt() + "," + valObj.getDeltaPriceOutletPct() + "," + valObj.getDiscMaxPct() + ",'" + valObj.getWarrantyType().replaceAll("'", "''") + "'," + valObj.getWarrantyPeriod() + "," + valObj.getShelfLife() + "," + valObj.getCommissionPctSales1() + "," + valObj.getCommissionPctSales2() + "," + valObj.getCommissionPctSales3() + "," + valObj.getCommissionPctSales4() + "," + valObj.getCommissionPctPartner() + "," + valObj.getCommissionPctGP() + ",'" + valObj.getLogicReorder().replaceAll("'", "''") + "','" + valObj.getLogicMaxQty().replaceAll("'", "''") + "'," + valObj.getThresholdQtyReorder() + "," + valObj.getThresholdQtyMaxQty() + ",'" + valObj.getRemarks1().replaceAll("'", "''") + "','" + valObj.getRemarks2().replaceAll("'", "''") + "','" + valObj.getKeywords().replaceAll("'", "''") + "','" + valObj.getPrefixLogic().replaceAll("'", "''") + "'," + valObj.getPrefixLength() + ",'" + valObj.getPrefixCode().replaceAll("'", "''") + "','" + valObj.getPostfixLogic().replaceAll("'", "''") + "'," + valObj.getPostfixLength() + ",'" + valObj.getPostfixCode().replaceAll("'", "''") + "','" + valObj.getCodeProject().replaceAll("'", "''") + "','" + valObj.getCodeDepartment().replaceAll("'", "''") + "','" + valObj.getCodeDealer().replaceAll("'", "''") + "','" + valObj.getCodeSalesman().replaceAll("'", "''") + "','" + valObj.getTaxOption().replaceAll("'", "''") + "'," + valObj.getTaxRate() + "," + valObj.getPriceEcom() + "," + valObj.getOutQty() + ",'" + valObj.getOutUnit().replaceAll("'", "''") + "'," + valObj.getInQty() + ",'" + valObj.getInUnit().replaceAll("'", "''") + "'," + valObj.getInnQty() + ",'" + valObj.getInnUnit().replaceAll("'", "''") + "'," + valObj.getInmQty() + ",'" + valObj.getInmUnit().replaceAll("'", "''") + "' " + ")";
            System.err.println(insertStatement);
            s.execute(insertStatement);
            conn.commit();
            closeConnection(conn);
        } catch (Throwable e) {
            e.printStackTrace();
            closeConnection(conn);
        }
    }

    public void updateObject(ItemObject valObj) {
        checkTable();
        Connection conn = null;
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            s.execute("update " + TABLENAME + " set " + ITEM_CODE + " = '" + valObj.getCode() + "' ," + NAME + " = '" + valObj.getName() + "' ," + DESCRIPTION + " = '" + valObj.getDescription() + "' ," + STATUS + " = '" + valObj.getStatus() + "' ," + LASTUPDATE + " = '" + valObj.getLastUpdate() + "' ," + USERID_EDIT + " = '" + valObj.getUserIdUpdate() + "' ," + UOM + " = '" + valObj.getUom() + "' ," + UNIT_OF_MEASURE + " = '" + valObj.getUnitOfMeasure() + "' ," + INV_TYPE + " = '" + valObj.getEnumInvType() + "' ," + ITEM_TYPE1 + " = '" + valObj.getItemType1() + "' ," + ITEM_TYPE2 + " = '" + valObj.getItemType2() + "' ," + ITEM_TYPE3 + " = '" + valObj.getItemType3() + "' ," + GLCODE + " = '" + valObj.getGlcode() + "' ," + CATEGORYID + " = '" + valObj.getCategoryId() + "' ," + STATE + " = '" + valObj.getState() + "' ," + CATEGORY1 + " = '" + valObj.getCategory1() + "' ," + CATEGORY2 + " = '" + valObj.getCategory2() + "' ," + CATEGORY3 + " = '" + valObj.getCategory3() + "' ," + CATEGORY4 + " = '" + valObj.getCategory4() + "' ," + CATEGORY5 + " = '" + valObj.getCategory5() + "' ," + HAS_CHILDREN + " = '" + (int) (valObj.isHasChildren() ? 1 : 0) + "' ," + PARENTID + " = '" + valObj.getParentId() + "' ," + PARENTRATIO + " = '" + valObj.getParentRatio() + "' ," + EANCODE + " = '" + valObj.getEanCode() + "' ," + UPCCODE + " = '" + valObj.getUpcCode() + "' ," + ISBNCODE + " = '" + valObj.getIsbnCode() + "' ," + PRICELIST + " = '" + valObj.getPriceList() + "' ," + PRICESALE + " = '" + valObj.getPriceSale() + "' ," + PRICEDISC1 + " = '" + valObj.getPriceDisc1() + "' ," + PRICEDISC2 + " = '" + valObj.getPriceDisc2() + "' ," + PRICEDISC3 + " = '" + valObj.getPriceDisc3() + "' ," + PRICEMIN + " = '" + valObj.getPriceMin() + "' ," + FIFOUNITCOST + " = '" + valObj.getFifoUnitCost() + "' ," + MAUNITCOST + " = '" + valObj.getMaUnitCost() + "' ," + WAUNITCOST + " = '" + valObj.getWaUnitCost() + "' ," + LASTUNITCOST + " = '" + valObj.getLastUnitCost() + "' ," + REPLACEMENTUNITCOST + " = '" + valObj.getReplacementUnitCost() + "' ," + SERIALIZED + " = '" + (int) (valObj.isSerialized() ? 1 : 0) + "' ," + WEIGHT + " = '" + valObj.getWeight() + "' ," + LENGTH + " = '" + valObj.getLength() + "' ," + WIDTH + " = '" + valObj.getWidth() + "' ," + DEPTH + " = '" + valObj.getDepth() + "' ," + PREFERREDSUPPLIER + " = '" + valObj.getPreferredSupplier() + "' ," + MINORDERQTY + " = '" + valObj.getMinOrderQty() + "' ," + LEADTIME + " = '" + valObj.getLeadTime() + "' ," + RESERVED1 + " = '" + valObj.getReserved1() + "' ," + RESERVED2 + " = '" + valObj.getReserved2() + "' ," + RESERVED3 + " = '" + valObj.getReserved3() + "' ," + PIX_THUMB_NAME + " = '" + valObj.getPixThumbName() + "' ," + PIX_FULL_NAME + " = '" + valObj.getPixFullName() + "' ," + PIX_THUMBNAIL + " = '" + valObj.getPixThumbnail() + "' ," + PIX_FULLSIZE + " = '" + valObj.getPixFullsize() + "' ," + REBATE1_PCT + " = '" + valObj.getRebate1Pct() + "' ," + REBATE1_PRICE + " = '" + valObj.getRebate1Price() + "' ," + REBATE1_START + " = '" + valObj.getRebate1Start() + "' ," + REBATE1_END + " = '" + valObj.getRebate1End() + "' ," + DISC1_PCT + " = '" + valObj.getDisc1Pct() + "' ," + DISC1_AMOUNT + " = '" + valObj.getDisc1Amount() + "' ," + DISC1_START + " = '" + valObj.getDisc1Start() + "' ," + DISC1_END + " = '" + valObj.getDisc1End() + "' ," + PRODUCTION_REQUIRED + " = '" + (int) (valObj.isProductionRequired() ? 1 : 0) + "' ," + PRODUCTION_LEAD_TIME + " = '" + valObj.getProductionLeadTime() + "' ," + PRODUCTION_COST + " = '" + valObj.getProductionCost() + "' ," + PRODUCTION_PROCESS + " = '" + valObj.getProductionProcess() + "' ," + PRODUCTION_NAME + " = '" + valObj.getProductionName() + "' ," + PRODUCTION_DESCRIPTION + " = '" + valObj.getProductionDescription() + "' ," + DELIVERY_REQUIRED + " = '" + (int) (valObj.isDeliveryRequired() ? 1 : 0) + "' ," + DELIVERY_LEAD_TIME + " = '" + valObj.getDeliveryLeadTime() + "' ," + DELIVERY_COST + " = '" + valObj.getDeliveryCost() + "' ," + DELIVERY_PROCESS + " = '" + valObj.getDeliveryProcess() + "' ," + DELIVERY_NAME + " = '" + valObj.getDeliveryName() + "' ," + DELIVERY_DESCRIPTION + " = '" + valObj.getDeliveryDescription() + "' ," + DELTA_PRICE_RETAIL_AMT + " = '" + valObj.getDeltaPriceRetailAmt() + "' ," + DELTA_PRICE_RETAIL_PCT + " = '" + valObj.getDeltaPriceRetailPct() + "' ," + DELTA_PRICE_DEALER_AMT + " = '" + valObj.getDeltaPriceDealerAmt() + "' ," + DELTA_PRICE_DEALER_PCT + " = '" + valObj.getDeltaPriceDealerPct() + "' ," + DELTA_PRICE_OUTLET_AMT + " = '" + valObj.getDeltaPriceOutletAmt() + "' ," + DELTA_PRICE_OUTLET_PCT + " = '" + valObj.getDeltaPriceOutletPct() + "' ," + DISC_MAX_PCT + " = '" + valObj.getDiscMaxPct() + "' ," + WARRANTY_TYPE + " = '" + valObj.getWarrantyType() + "' ," + WARRANTY_PERIOD + " = '" + valObj.getWarrantyPeriod() + "' ," + SHELF_LIFE + " = '" + valObj.getShelfLife() + "' ," + COMMISSION_PCT_SALES1 + " = '" + valObj.getCommissionPctSales1() + "' ," + COMMISSION_PCT_SALES2 + " = '" + valObj.getCommissionPctSales2() + "' ," + COMMISSION_PCT_SALES3 + " = '" + valObj.getCommissionPctSales3() + "' ," + COMMISSION_PCT_SALES4 + " = '" + valObj.getCommissionPctSales4() + "' ," + COMMISSION_PCT_PARTNER + " = '" + valObj.getCommissionPctPartner() + "' ," + COMMISSION_PCT_GP + " = '" + valObj.getCommissionPctGP() + "' ," + LOGIC_REORDER + " = '" + valObj.getLogicReorder() + "' ," + LOGIC_MAX_QTY + " = '" + valObj.getLogicMaxQty() + "' ," + THRESHOLD_QTY_REORDER + " = '" + valObj.getThresholdQtyReorder() + "' ," + THRESHOLD_QTY_MAX_QTY + " = '" + valObj.getThresholdQtyMaxQty() + "' ," + REMARKS1 + " = '" + valObj.getRemarks1() + "' ," + REMARKS2 + " = '" + valObj.getRemarks2() + "' ," + KEYWORDS + " = '" + valObj.getKeywords() + "' ," + PREFIX_LOGIC + " = '" + valObj.getPrefixLogic() + "' ," + PREFIX_LENGTH + " = '" + valObj.getPrefixLength() + "' ," + PREFIX_CODE + " = '" + valObj.getPrefixCode() + "' ," + POSTFIX_LOGIC + " = '" + valObj.getPostfixLogic() + "' ," + POSTFIX_LENGTH + " = '" + valObj.getPostfixLength() + "' ," + POSTFIX_CODE + " = '" + valObj.getPostfixCode() + "' ," + CODE_PROJECT + " = '" + valObj.getCodeProject() + "' ," + CODE_DEPARTMENT + " = '" + valObj.getCodeDepartment() + "' ," + CODE_DEALER + " = '" + valObj.getCodeDealer() + "' ," + CODE_SALESMAN + " = '" + valObj.getCodeSalesman() + "' ," + TAX_OPTION + " = '" + valObj.getTaxOption() + "' ," + TAX_RATE + " = '" + valObj.getTaxRate() + "' ," + PRICEECOM + " = '" + valObj.getPriceEcom() + "' ," + OUT_QTY + " = '" + valObj.getOutQty() + "' ," + OUT_UNIT + " = '" + valObj.getOutUnit() + "' ," + IN_QTY + " = '" + valObj.getInQty() + "' ," + IN_UNIT + " = '" + valObj.getInUnit() + "' ," + INN_QTY + " = '" + valObj.getInnQty() + "' ," + INN_UNIT + " = '" + valObj.getInnUnit() + "' ," + INM_QTY + " = '" + valObj.getInmQty() + "' ," + INM_UNIT + " = '" + valObj.getInmUnit() + "' WHERE " + PKID + " = '" + valObj.getPkid() + "'");
            closeConnection(conn);
        } catch (Throwable e) {
            System.out.println("exception thrown:");
            closeConnection(conn);
        }
    }

    public ArrayList selectObject(ArrayList filter) {
        checkTable();
        ArrayList target = new ArrayList();
        ItemObject result = new ItemObject();
        Connection conn = null;
        checkTable();
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            String selectStatement = "SELECT * FROM " + TABLENAME + " ";
            if (filter.size() > 0) {
                selectStatement = selectStatement + " WHERE ";
                for (int i = 0; i < filter.size(); i++) {
                    if (i == filter.size() - 1) {
                        selectStatement = selectStatement + filter.get(i);
                    } else {
                        selectStatement = selectStatement + filter.get(i) + " AND ";
                    }
                }
            }
            ResultSet rs = s.executeQuery(selectStatement);
            while (rs.next()) {
                result = new ItemObject();
                result.setPkid(new Integer(rs.getInt(PKID)));
                result.setCode(rs.getString(ITEM_CODE));
                result.setName(rs.getString(NAME));
                result.setDescription(rs.getString(DESCRIPTION));
                result.setStatus(rs.getString(STATUS));
                result.setLastUpdate(rs.getTimestamp(LASTUPDATE).getTime());
                result.setUserIdUpdate(new Integer(rs.getInt(USERID_EDIT)));
                result.setUom(rs.getString(UOM));
                result.setUnitOfMeasure(new Integer(rs.getInt(UNIT_OF_MEASURE)));
                result.setEnumInvType(new Integer(rs.getInt(INV_TYPE)));
                result.setItemType1(rs.getString(ITEM_TYPE1));
                result.setItemType2(rs.getString(ITEM_TYPE2));
                result.setItemType3(rs.getString(ITEM_TYPE3));
                result.setGlcode(rs.getString(GLCODE));
                result.setCategoryId(new Integer(rs.getInt(CATEGORYID)));
                result.setState(rs.getString(STATE));
                result.setCategory1(rs.getString(CATEGORY1));
                result.setCategory2(rs.getString(CATEGORY2));
                result.setCategory3(rs.getString(CATEGORY3));
                result.setCategory4(rs.getString(CATEGORY4));
                result.setCategory5(rs.getString(CATEGORY5));
                result.setHasChildren((boolean) (rs.getInt(HAS_CHILDREN) == 1 ? true : false));
                result.setParentId(new Integer(rs.getInt(PARENTID)));
                result.setParentRatio(rs.getBigDecimal(PARENTRATIO));
                result.setEanCode(rs.getString(EANCODE));
                result.setUpcCode(rs.getString(UPCCODE));
                result.setIsbnCode(rs.getString(ISBNCODE));
                result.setPriceList(rs.getBigDecimal(PRICELIST));
                result.setPriceSale(rs.getBigDecimal(PRICESALE));
                result.setPriceDisc1(rs.getBigDecimal(PRICEDISC1));
                result.setPriceDisc2(rs.getBigDecimal(PRICEDISC2));
                result.setPriceDisc3(rs.getBigDecimal(PRICEDISC3));
                result.setPriceMin(rs.getBigDecimal(PRICEMIN));
                result.setFifoUnitCost(rs.getBigDecimal(FIFOUNITCOST));
                result.setMaUnitCost(rs.getBigDecimal(MAUNITCOST));
                result.setWaUnitCost(rs.getBigDecimal(WAUNITCOST));
                result.setLastUnitCost(rs.getBigDecimal(LASTUNITCOST));
                result.setReplacementUnitCost(rs.getBigDecimal(REPLACEMENTUNITCOST));
                result.setSerialized((boolean) (rs.getInt(SERIALIZED) == 1 ? true : false));
                result.setWeight(rs.getBigDecimal(WEIGHT));
                result.setLength(rs.getBigDecimal(LENGTH));
                result.setWidth(rs.getBigDecimal(WIDTH));
                result.setDepth(rs.getBigDecimal(DEPTH));
                result.setMinOrderQty(rs.getBigDecimal(MINORDERQTY));
                result.setPreferredSupplier(new Integer(rs.getInt(PREFERREDSUPPLIER)));
                result.setLeadTime(new Long(rs.getLong(LEADTIME)));
                result.setReserved1(rs.getString(RESERVED1));
                result.setReserved2(rs.getString(RESERVED2));
                result.setReserved3(rs.getString(RESERVED3));
                result.setPixThumbName(rs.getString(PIX_THUMB_NAME));
                result.setPixFullName(rs.getString(PIX_FULL_NAME));
                result.setRebate1Pct(rs.getBigDecimal(REBATE1_PCT));
                result.setRebate1Price(rs.getBigDecimal(REBATE1_PRICE));
                result.setRebate1Start(rs.getTimestamp(REBATE1_START).getTime());
                result.setRebate1End(rs.getTimestamp(REBATE1_END).getTime());
                result.setDisc1Pct(rs.getBigDecimal(DISC1_PCT));
                result.setDisc1Amount(rs.getBigDecimal(DISC1_AMOUNT));
                result.setDisc1Start(rs.getTimestamp(DISC1_START).getTime());
                result.setDisc1End(rs.getTimestamp(DISC1_END).getTime());
                result.setProductionRequired((boolean) (rs.getInt(PRODUCTION_REQUIRED) == 1 ? true : false));
                result.setProductionLeadTime(new Long(rs.getLong(PRODUCTION_LEAD_TIME)));
                result.setProductionCost(rs.getBigDecimal(PRODUCTION_COST));
                result.setProductionProcess(rs.getString(PRODUCTION_PROCESS));
                result.setProductionName(rs.getString(PRODUCTION_NAME));
                result.setProductionDescription(rs.getString(PRODUCTION_DESCRIPTION));
                result.setDeliveryRequired((boolean) (rs.getInt(DELIVERY_REQUIRED) == 1 ? true : false));
                result.setDeliveryLeadTime(new Long(rs.getLong(DELIVERY_LEAD_TIME)));
                result.setDeliveryCost(rs.getBigDecimal(DELIVERY_COST));
                result.setDeliveryProcess(rs.getString(DELIVERY_PROCESS));
                result.setDeliveryName(rs.getString(DELIVERY_NAME));
                result.setDeliveryDescription(rs.getString(DELIVERY_DESCRIPTION));
                result.setDeltaPriceRetailAmt(rs.getBigDecimal(DELTA_PRICE_RETAIL_AMT));
                result.setDeltaPriceRetailPct(rs.getBigDecimal(DELTA_PRICE_RETAIL_PCT));
                result.setDeltaPriceDealerAmt(rs.getBigDecimal(DELTA_PRICE_DEALER_AMT));
                result.setDeltaPriceDealerPct(rs.getBigDecimal(DELTA_PRICE_DEALER_PCT));
                result.setDeltaPriceOutletAmt(rs.getBigDecimal(DELTA_PRICE_OUTLET_AMT));
                result.setDeltaPriceOutletPct(rs.getBigDecimal(DELTA_PRICE_OUTLET_PCT));
                result.setDiscMaxPct(rs.getBigDecimal(DISC_MAX_PCT));
                result.setWarrantyType(rs.getString(WARRANTY_TYPE));
                result.setWarrantyPeriod(new Integer(rs.getInt(WARRANTY_PERIOD)));
                result.setShelfLife(new Integer(rs.getInt(SHELF_LIFE)));
                result.setCommissionPctSales1(rs.getBigDecimal(COMMISSION_PCT_SALES1));
                result.setCommissionPctSales2(rs.getBigDecimal(COMMISSION_PCT_SALES2));
                result.setCommissionPctSales3(rs.getBigDecimal(COMMISSION_PCT_SALES3));
                result.setCommissionPctSales4(rs.getBigDecimal(COMMISSION_PCT_SALES4));
                result.setCommissionPctPartner(rs.getBigDecimal(COMMISSION_PCT_PARTNER));
                result.setCommissionPctGP(rs.getBigDecimal(COMMISSION_PCT_GP));
                result.setLogicReorder(rs.getString(LOGIC_REORDER));
                result.setLogicMaxQty(rs.getString(LOGIC_MAX_QTY));
                result.setThresholdQtyReorder(rs.getBigDecimal(THRESHOLD_QTY_REORDER));
                result.setThresholdQtyMaxQty(rs.getBigDecimal(THRESHOLD_QTY_MAX_QTY));
                result.setRemarks1(rs.getString(REMARKS1));
                result.setRemarks2(rs.getString(REMARKS2));
                result.setKeywords(rs.getString(KEYWORDS));
                result.setPrefixLogic(rs.getString(PREFIX_LOGIC));
                result.setPrefixLength(new Integer(rs.getInt(PREFIX_LENGTH)));
                result.setPrefixCode(rs.getString(PREFIX_CODE));
                result.setPostfixLogic(rs.getString(POSTFIX_LOGIC));
                result.setPostfixLength(new Integer(rs.getInt(POSTFIX_LENGTH)));
                result.setPostfixCode(rs.getString(POSTFIX_CODE));
                result.setCodeProject(rs.getString(CODE_PROJECT));
                result.setCodeDepartment(rs.getString(CODE_DEPARTMENT));
                result.setCodeDealer(rs.getString(CODE_DEALER));
                result.setCodeSalesman(rs.getString(CODE_SALESMAN));
                result.setTaxOption(rs.getString(TAX_OPTION));
                result.setTaxRate(rs.getBigDecimal(TAX_RATE));
                result.setPriceEcom(rs.getBigDecimal(PRICEECOM));
                result.setOutQty(rs.getBigDecimal(OUT_QTY));
                result.setOutUnit(rs.getString(OUT_UNIT));
                result.setInQty(rs.getBigDecimal(IN_QTY));
                result.setInUnit(rs.getString(IN_UNIT));
                result.setInnQty(rs.getBigDecimal(INN_QTY));
                result.setInnUnit(rs.getString(INN_UNIT));
                result.setInmQty(rs.getBigDecimal(INM_QTY));
                result.setInmUnit(rs.getString(INM_UNIT));
                target.add(result);
            }
            rs.close();
            s.close();
            closeConnection(conn);
        } catch (Throwable e) {
            e.printStackTrace();
            closeConnection(conn);
        }
        return target;
    }

    public void deleteObject(Integer pkid) {
        checkTable();
        Connection conn = null;
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            s.execute("delete from " + TABLENAME + " where pkid =" + pkid.toString());
            closeConnection(conn);
        } catch (Throwable e) {
            e.printStackTrace();
            closeConnection(conn);
        }
    }

    public ArrayList getItemSearchResult(String val) {
        checkTable();
        ArrayList target = new ArrayList();
        ItemSearchRow result = new ItemSearchRow();
        Connection conn = null;
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            String selectStatement = "SELECT a.pkid, a.item_code, b.bal, a.description, a.name FROM " + TABLENAME + " a " + " INNER JOIN INV_STOCK b ON (a.pkid = b.itemid) " + " WHERE (lower(a.item_code) LIKE '%" + val.toLowerCase() + "%' OR lower(a.name) LIKE '%" + val.toLowerCase() + "%')" + " OR lower(a.description) LIKE '%" + val.toLowerCase() + "%' ";
            System.out.println(selectStatement);
            ResultSet rs = s.executeQuery(selectStatement);
            while (rs.next()) {
                result = new ItemSearchRow();
                result.itemCode = rs.getString(ITEM_CODE);
                result.description = rs.getString(NAME);
                result.qty = rs.getBigDecimal(StockDAO.BALANCE).toString();
                result.pkid = new Integer(rs.getInt(PKID));
                target.add(result);
            }
            rs.close();
            s.close();
            closeConnection(conn);
        } catch (Throwable e) {
            closeConnection(conn);
        }
        System.err.println("Returning " + target.size() + " results");
        return target;
    }

    public Timestamp getLatestTimestamp() {
        checkTable();
        Connection conn = null;
        Timestamp result = TimeFormat.getTimestamp();
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            s.setMaxRows(1);
            String selectStatement = "SELECT lastupdate FROM " + TABLENAME + " ORDER BY lastupdate DESC";
            System.out.println(selectStatement);
            ResultSet rs = s.executeQuery(selectStatement);
            while (rs.next()) {
                result = rs.getTimestamp(LASTUPDATE);
            }
            rs.close();
            s.close();
            closeConnection(conn);
        } catch (Throwable e) {
            closeConnection(conn);
        }
        return result;
    }

    public boolean hasRows() {
        Connection conn = null;
        boolean result = false;
        try {
            conn = getConnnection();
            Statement s = conn.createStatement();
            s.setMaxRows(1);
            String selectStatement = "SELECT * FROM " + TABLENAME;
            System.out.println(selectStatement);
            ResultSet rs = s.executeQuery(selectStatement);
            while (rs.next()) {
                result = true;
            }
            rs.close();
            s.close();
            closeConnection(conn);
        } catch (Throwable e) {
            closeConnection(conn);
        }
        return result;
    }
}
