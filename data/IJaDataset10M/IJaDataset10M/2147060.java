package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.process.DocAction;
import org.openXpertya.process.DocumentEngine;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Fraccionamiento de Artículo
 * 
 * @author Franco Bonafine - Disytel
 */
public class MSplitting extends X_M_Splitting implements DocAction {

    /** Cache de líneas del fraccionamiento */
    private List<MSplittingLine> lines = null;

    /**
	 * Constructor de la clase
	 * @param ctx
	 * @param M_Splitting_ID
	 * @param trxName
	 */
    public MSplitting(Properties ctx, int M_Splitting_ID, String trxName) {
        super(ctx, M_Splitting_ID, trxName);
        if (M_Splitting_ID == 0) {
            setDateTrx(new Timestamp(System.currentTimeMillis()));
            setProductQty(BigDecimal.ZERO);
            setShrinkQty(BigDecimal.ZERO);
            setSplitQty(BigDecimal.ZERO);
            setConvertedProductQty(BigDecimal.ZERO);
            setConvertedShrinkQty(BigDecimal.ZERO);
            setConvertedSplitQty(BigDecimal.ZERO);
            setDocStatus(DOCSTATUS_InProgress);
            setDocAction(DOCACTION_Complete);
            setProcessed(false);
        }
    }

    /**
	 * Constructor de la clase
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
    public MSplitting(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        if (is_ValueChanged("M_Warehouse_ID") && hasLines()) {
            log.saveError("SaveError", Msg.translate(getCtx(), "CannotChangeSplitWarehouse"));
            return false;
        }
        if (getProduct().getProductFractions().isEmpty()) {
            log.saveError("SaveError", Msg.translate(getCtx(), "SplitProductNeedFractions"));
            return false;
        }
        if (getProduct().getUOMConversions().isEmpty()) {
            log.saveError("SaveError", Msg.translate(getCtx(), "SplitProductNeedConversions"));
            return false;
        }
        if (getProductQty().compareTo(BigDecimal.ZERO) <= 0) {
            log.saveError("SaveError", Msg.getMsg(getCtx(), "ValueMustBeGreatherThanZero", new Object[] { Msg.translate(getCtx(), "Quantity"), "0" }));
            return false;
        }
        if ((is_ValueChanged("M_Product_ID") || is_ValueChanged("C_Conversion_UOM_ID")) && hasLines()) {
            log.saveError("SaveError", Msg.translate(getCtx(), "CannotChangeProductOrUOM"));
            return false;
        }
        if (is_ValueChanged("M_Product_ID") || is_ValueChanged("ProductQty") || is_ValueChanged("C_Conversion_UOM_ID")) {
            calculateQuantities();
        }
        if (is_ValueChanged("ProductQty") && getConvertedShrinkQty().compareTo(BigDecimal.ZERO) < 0) {
            log.saveError("SaveError", Msg.translate(getCtx(), "SplitQtyUnderLinesQty"));
            return false;
        }
        return true;
    }

    /**
	 * Realiza validaciones sobre una línea del fraccionamiento antes de ser
	 * guardada.
	 * @param line Línea del fraccionamiento 
	 * @return true si la línea no contiene errores, false en caso contrario.
	 * En caso de error se setea en el log el mensaje correspondiente.
	 */
    protected boolean beforeSaveLine(MSplittingLine line) {
        BigDecimal convertedAvailableQty = getConvertedAvailableQty(line);
        if (line.getConvertedQty().compareTo(convertedAvailableQty) > 0) {
            log.saveError("SaveError", Msg.getMsg(getCtx(), "SplitQuantityOverflow", new Object[] { line.getConvertedQty(), convertedAvailableQty }));
            return false;
        }
        return true;
    }

    /**
	 * Actualización de los valores del fraccionamiento. Este método es invocado
	 * luego de que se guarda una línea en este fraccionamiento.
	 */
    protected void update() {
        calculateQuantities();
    }

    /**
	 * @return Devuelve el artículo a fraccionar.
	 */
    public MProduct getProduct() {
        return MProduct.get(getCtx(), getM_Product_ID());
    }

    /**
	 * Calcula y asigna las cantidades del fraccionamiento.
	 */
    private void calculateQuantities() {
        calculateConvertedSplitQty();
        setConvertedProductQty(convertToConversionUOM(getProductQty()));
        setConvertedShrinkQty(getConvertedProductQty().subtract(getConvertedSplitQty()));
        setSplitQty(convertToProductUOM(getConvertedSplitQty()));
        setShrinkQty(convertToProductUOM(getConvertedShrinkQty()));
    }

    /**
	 * Calcula la cantidad fraccionada convertida a la UM de conversión, a partir
	 * de las cantidades de las líneas del fraccionamiento.
	 */
    private void calculateConvertedSplitQty() {
        BigDecimal convertedSplitQty = BigDecimal.ZERO;
        for (MSplittingLine line : getLines(true)) {
            convertedSplitQty = convertedSplitQty.add(line.getConvertedQty());
        }
        setConvertedSplitQty(convertedSplitQty);
    }

    /**
	 * Calcula y devuelve la cantidad disponible para fraccionamiento teniendo
	 * en cuenta una determinada línea. La cantidad devuelta va a estar ponderada
	 * por la cantidad de la línea. Si <code>forLine</code> es <code>null</code>
	 * entonces la cantidad disponible es exacamente igual a la cantidad de merma.
	 * @param forLine Línea para la cual que se quiere obtener la cantidad disponible.
	 * Puede ser null.
	 * @return Cantidad disponible según línea o merma si la línea es null.
	 */
    private BigDecimal getConvertedAvailableQty(MSplittingLine forLine) {
        BigDecimal convertedAvailableQty = BigDecimal.ZERO;
        if (forLine == null) {
            convertedAvailableQty = getConvertedShrinkQty();
        } else {
            BigDecimal convertedSplitQty = BigDecimal.ZERO;
            for (MSplittingLine line : getLines(true)) {
                if (line.getM_SplittingLine_ID() != forLine.getM_SplittingLine_ID()) {
                    convertedSplitQty = convertedSplitQty.add(line.getConvertedQty());
                }
            }
            convertedAvailableQty = getConvertedProductQty().subtract(convertedSplitQty);
        }
        return convertedAvailableQty;
    }

    /**
	 * Realiza la conversión de una cantidad expresada en la UM del artículo
	 * hacia la UM común de conversión, según las conversiones configuradas
	 * para el artículo a fraccionar.
	 * @param quantity Cantidad a convertir
	 * @return Cantidad convertida
	 */
    private BigDecimal convertToConversionUOM(BigDecimal quantity) {
        return convertToConversionUOM(quantity, getM_Product_ID());
    }

    /**
	 * Realiza la conversión de una cantidad expresada en la UM de conversión
	 * del fraccionamiento hacia la UM del artículo, según las conversiones configuradas
	 * para el artículo a fraccionar.
	 * @param quantity Cantidad a convertir
	 * @return Cantidad convertida
	 */
    private BigDecimal convertToProductUOM(BigDecimal quantity) {
        return convertToProductUOM(quantity, getM_Product_ID());
    }

    /**
	 * Realiza la conversión de una cantidad para un artículo determinado hacia
	 * la UM común de conversión del fraccionamiento. Como UM origen se toma la UM
	 * definida para el artículo.
	 * @param quantity Cantidad a Convertir
	 * @param productID ID del artículo cuya cantidad se quiere convertir
	 * @return Cantidad convertida a la UM común de conversión del fraccionamiento
	 */
    public BigDecimal convertToConversionUOM(BigDecimal quantity, Integer productID) {
        BigDecimal convertedQuantity = MUOMConversion.convertProductFrom(getCtx(), productID, getC_Conversion_UOM_ID(), quantity);
        return convertedQuantity;
    }

    /**
	 * Realiza la conversión de una cantidad expresada en la UM de conversión del 
	 * fraccionamiento hacia la UM del artículo, para un artículo determinado. 
	 * @param quantity Cantidad a Convertir
	 * @param productID ID del artículo cuya cantidad se quiere convertir
	 * @return Cantidad convertida a la UM del artículo.
	 */
    public BigDecimal convertToProductUOM(BigDecimal quantity, Integer productID) {
        BigDecimal convertedQuantity = MUOMConversion.convertProductTo(getCtx(), productID, getC_Conversion_UOM_ID(), quantity);
        return convertedQuantity;
    }

    /**
	 * Devuelve las líneas de este fraccionamiento
	 * @param reload Indica si se deben recargar las líneas desde la BD
	 * @return Lista con las líneas del fraccionamiento. Si no tiene
	 * líneas devuelve una lista cuyo@param reload tamaño es cero.
	 */
    public List<MSplittingLine> getLines(boolean reload) {
        if (lines == null || reload) {
            lines = new ArrayList<MSplittingLine>();
            String sql = "SELECT * FROM M_SplittingLine WHERE M_Splitting_ID = ?";
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = DB.prepareStatement(sql);
                pstmt.setInt(1, getM_Splitting_ID());
                rs = pstmt.executeQuery();
                MSplittingLine line = null;
                while (rs.next()) {
                    line = new MSplittingLine(getCtx(), rs, get_TrxName());
                    lines.add(line);
                }
            } catch (SQLException e) {
                log.log(Level.SEVERE, "Get Splitting Lines Error", e);
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                } catch (Exception e) {
                }
            }
        }
        return lines;
    }

    /**
	 * @return Devuelve las líneas del fraccionamiento. NO recarga
	 * la información de la base de datos. Se devuelven las líneas
	 * cacheadas en este objeto.
	 */
    public List<MSplittingLine> getLines() {
        return getLines(false);
    }

    /**
	 * @return Indica si este fraccionamiento contiene líneas.
	 */
    public boolean hasLines() {
        int linesCount = DB.getSQLValue(get_TrxName(), "SELECT COUNT(*) FROM M_SplittingLine WHERE M_Splitting_ID = ?", getM_Splitting_ID());
        return linesCount > 0;
    }

    /**
	 * @return Indica si este fraccionamiento tiene merma.
	 */
    public boolean hasShrink() {
        return getSplitQty().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
	 * @return Devuelve el almacén asociado a este fraccionamiento.
	 */
    public MWarehouse getWarehouse() {
        return new MWarehouse(getCtx(), getM_Warehouse_ID(), get_TrxName());
    }

    /**
	 * @return Devuelve el mensaje de descripción para el inventario
	 * generado a partir de este fraccionamiento.
	 */
    private String getInventoryDescription(boolean splittingVoid) {
        String msg = "SplittingInventoryDescription";
        if (splittingVoid) {
            msg = "SplittingVoidInventoryDescription";
        }
        return Msg.getMsg(getCtx(), msg, new Object[] { getDocumentNo(), getProduct().getName() });
    }

    /**
	 * @return Devuelve el inventario generado a partir del completado
	 * de este fraccionamiento. Si aún no se completó el fraccionamiento
	 * devuelve null.
	 */
    public MInventory getInventory() {
        MInventory inventory = null;
        if (getM_Inventory_ID() > 0) {
            inventory = new MInventory(getCtx(), getM_Inventory_ID(), get_TrxName());
            inventory.setCallerDocument(this);
        }
        return inventory;
    }

    /**
	 * @return Devuelve el inventario generado a partir de la anulación
	 * de este fraccionamiento. Si aún no se anuló el fraccionamiento
	 * devuelve null.
	 */
    public MInventory getVoidInventory() {
        MInventory inventory = null;
        if (getVoid_Inventory_ID() > 0) {
            inventory = new MInventory(getCtx(), getVoid_Inventory_ID(), get_TrxName());
            inventory.setCallerDocument(this);
        }
        return inventory;
    }

    @Override
    public boolean processIt(String action) throws Exception {
        m_processMsg = null;
        DocumentEngine engine = new DocumentEngine(this, getDocStatus());
        return engine.processIt(action, getDocAction(), log);
    }

    @Override
    public boolean voidIt() {
        if (getM_Inventory_ID() > 0) {
            try {
                createInventory(true);
                closeInventories();
            } catch (Exception e) {
                m_processMsg = e.getMessage();
                return false;
            }
        }
        setProcessed(true);
        setDocAction(DOCACTION_None);
        return true;
    }

    @Override
    public boolean closeIt() {
        try {
            closeInventories();
        } catch (Exception e) {
            m_processMsg = e.getMessage();
            return false;
        }
        setDocAction(DOCACTION_None);
        return true;
    }

    /**
	 * Ciera los inventarios asociados a este fraccionamiento.
	 * @throws Exception si se produce un error al cerrar o guardar
	 * alguno de los inventarios.
	 */
    private void closeInventories() throws Exception {
        if (getM_Inventory_ID() > 0) {
            closeInventory(getInventory());
        }
        if (getVoid_Inventory_ID() > 0) {
            closeInventory(getVoidInventory());
        }
    }

    /**
	 * Cierra un inventario.
	 * @throws Exception si se produce un error en el procesamiento de la acción
	 * o en el guardado de los cambios.
	 */
    private void closeInventory(MInventory inventory) throws Exception {
        String error = null;
        if (!inventory.processIt(MInventory.DOCACTION_Close)) {
            error = inventory.getProcessMsg();
            if (error == null) {
                error = "...";
            }
        } else if (!inventory.save()) {
            error = CLogger.retrieveErrorAsString();
        }
        if (error != null) {
            throw new Exception("@CloseInventoryError@ (" + inventory.getDocumentNo() + "): " + error);
        }
    }

    @Override
    public String prepareIt() {
        if (!hasLines()) {
            m_processMsg = "@NoLines@";
            return STATUS_Invalid;
        }
        if (getSplitQty().add(getShrinkQty()).compareTo(getProductQty()) != 0) {
            m_processMsg = "@SplitInvalidQuantities@";
            return STATUS_Invalid;
        }
        MWarehouse warehouse = getWarehouse();
        if (warehouse.getSplitting_Charge_ID() == 0 || (hasShrink() && warehouse.getShrink_Charge_ID() == 0)) {
            m_processMsg = "@SplittingChargesRequired@";
            return STATUS_Invalid;
        }
        if (MWarehouseClose.isWarehouseCloseControlActivated() && getDateTrx().compareTo(Env.getDate()) < 0) {
            setDateTrx(Env.getDate());
        }
        setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }

    @Override
    public String completeIt() {
        try {
            createInventory(false);
        } catch (Exception e) {
            m_processMsg = e.getMessage();
            return STATUS_Invalid;
        }
        setProcessed(true);
        setDocAction(DOCACTION_Void);
        return DOCSTATUS_Completed;
    }

    /**
	 * Crea el inventario que da de alta / baja los artículos según el fraccionamiento
	 * @param splittingVoid Indica si se está anulando el fraccionamiento. En ese caso, 
	 * se creará un inventario físico que revierta las cantidades de los artículos
	 * tal como estaban antes de crear y completar este fraccionamiento.
	 * @throws Exception Cuando se produce algún error al guardar o procesar el encabezado
	 * o líneas del inventario.
	 */
    private void createInventory(boolean splittingVoid) throws Exception {
        MWarehouse warehouse = getWarehouse();
        MInventory inventory = new MInventory(warehouse);
        inventory.setMovementDate(getDateTrx());
        inventory.setDescription(getInventoryDescription(splittingVoid));
        if (!inventory.save()) {
            throw new Exception("@InventoryCreateError@: " + CLogger.retrieveErrorAsString());
        }
        createInventoryLine(inventory, getProduct(), getM_Locator_ID(), getSplitQty().negate(), warehouse.getSplitting_Charge_ID(), splittingVoid);
        createInventoryLine(inventory, getProduct(), getM_Locator_ID(), getShrinkQty().negate(), warehouse.getShrink_Charge_ID(), splittingVoid);
        for (MSplittingLine splittingLine : getLines(true)) {
            createInventoryLine(inventory, splittingLine.getProductTo(), splittingLine.getM_Locator_ID(), splittingLine.getProductQty(), warehouse.getSplitting_Charge_ID(), splittingVoid);
        }
        String error = null;
        if (!inventory.processIt(MInventory.DOCACTION_Complete)) {
            error = inventory.getProcessMsg();
        } else if (!inventory.save()) {
            error = CLogger.retrieveErrorAsString();
        }
        if (error != null) {
            throw new Exception("@InventoryCompleteError@: " + error);
        }
        if (!splittingVoid) {
            setM_Inventory_ID(inventory.getM_Inventory_ID());
        } else {
            setVoid_Inventory_ID(inventory.getM_Inventory_ID());
        }
    }

    /**
	 * Crea y guarda una línea de un inventario.
	 * @param inventory Invertario contenedor de la línea
	 * @param product Artículo de la línea
	 * @param locatorID Ubicación dentro del almacén
	 * @param qty Cantidad de la línea (si es positiva suma stock, si es
	 * negativa resta stock)
	 * @param chargeID Cargo para contabilización de la línea
	 * @param splittingVoid Indica si se está anulando este fraccionamiento, en cuyo
	 * caso invierte los signos de las cantidades para generar líneas inversas a las
	 * líneas creadas para el inventario generado al completar este fraccionamiento.
	 * @throws Exception Cuando se produce un error en el guardado de la línea.
	 */
    private void createInventoryLine(MInventory inventory, MProduct product, Integer locatorID, BigDecimal qty, Integer chargeID, boolean splittingVoid) throws Exception {
        MInventoryLine inventoryLine = new MInventoryLine(inventory, locatorID, product.getM_Product_ID(), 0, BigDecimal.ZERO, BigDecimal.ZERO);
        inventoryLine.setInventoryType(MInventoryLine.INVENTORYTYPE_ChargeAccount);
        inventoryLine.setC_Charge_ID(chargeID);
        BigDecimal lineQty = qty.negate();
        if (splittingVoid) {
            lineQty = lineQty.negate();
        }
        inventoryLine.setQtyInternalUse(lineQty);
        if (!inventoryLine.save()) {
            throw new Exception("@InventoryLineCreateError@ (" + product.getName() + "): " + CLogger.retrieveErrorAsString());
        }
    }

    @Override
    public boolean approveIt() {
        return false;
    }

    @Override
    public BigDecimal getApprovalAmt() {
        return null;
    }

    @Override
    public int getC_Currency_ID() {
        return 0;
    }

    @Override
    public int getDoc_User_ID() {
        return 0;
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public boolean invalidateIt() {
        return false;
    }

    @Override
    public boolean postIt() {
        return false;
    }

    @Override
    public boolean reActivateIt() {
        return false;
    }

    @Override
    public boolean rejectIt() {
        return false;
    }

    @Override
    public boolean reverseAccrualIt() {
        return false;
    }

    @Override
    public boolean reverseCorrectIt() {
        return false;
    }

    @Override
    public boolean unlockIt() {
        return false;
    }
}
