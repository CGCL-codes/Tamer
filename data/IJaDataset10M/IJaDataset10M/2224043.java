package net.sf.eqemutils.market;

import java.util.*;
import net.sf.eqemutils.eqemu.*;
import net.sf.eqemutils.webapp.*;
import net.sf.eqemutils.utils.*;

/** 
 */
public class InventoryRenderTemplate {

    public static InventoryRenderTemplate LeftSideBankTemplate(WebApplication Application) {
        InventoryRenderTemplate Result;
        Result = new InventoryRenderTemplate();
        Result._OnlyTransferMarketable = true;
        Result._ContainerSeparatorLine = VectorUtils.InitWithElement("<tr class='myline' height='1'><td colspan='3'></td></tr>");
        Result._TopLevelEmptySlotTemplate = VectorUtils.InitWithElement("<td colspan='2'><li>empty " + ItemSize.GIANT + " slot</li></td><td></td>");
        Result._TopLevelFilledSlotTemplate = VectorUtils.InitWithElement("<td colspan='2'><li><b><a href='" + Application.BrowserItemByIdUrlPrefix() + "${Id}'>${Name} (# ${Id})</a></b></li></td>");
        Result._TopLevelFilledStackTemplate = VectorUtils.InitWithElement("<td colspan='2'><li><b><a href='" + Application.BrowserItemByIdUrlPrefix() + "${Id}'>${Amount} x ${Name} (# ${Id})</a></b></li></td>");
        Result._TopLevelNonTransferableSlotPreTemplate = new Vector<String>();
        Result._TopLevelTransferableSlotPreTemplate = new Vector<String>();
        Result._TopLevelNonTransferableSlotPostTemplate = new Vector<String>();
        Result._TopLevelNonTransferableSlotPostTemplate.addElement("<td nowrap='1'>");
        Result._TopLevelNonTransferableSlotPostTemplate.addElement("<input type='hidden' value='${Id}' name='left-status-slot-${SlotNo}-id'/></b>");
        Result._TopLevelNonTransferableSlotPostTemplate.addElement("<input type='hidden' value='${Amount}' name='left-status-slot-${SlotNo}-amount'/></b>");
        Result._TopLevelNonTransferableSlotPostTemplate.addElement("</td>");
        Result._TopLevelTransferableSlotPostTemplate = new Vector<String>();
        Result._TopLevelTransferableSlotPostTemplate.addElement("<td nowrap='1'>");
        Result._TopLevelTransferableSlotPostTemplate.addElement("<input type='checkbox' name='left-transfer-slot-${SlotNo}' />");
        Result._TopLevelTransferableSlotPostTemplate.addElement("<input type='text' value='${Amount}' size='4' name='left-transfer-slot-${SlotNo}-amount-to-transfer' class='form'/><b>&nbsp;&gt;&gt;&gt;</b>");
        Result._TopLevelTransferableSlotPostTemplate.addElement("<input type='hidden' value='${Id}' name='left-status-slot-${SlotNo}-id'/>");
        Result._TopLevelTransferableSlotPostTemplate.addElement("<input type='hidden' value='${Amount}' name='left-status-slot-${SlotNo}-amount'/>");
        Result._TopLevelTransferableSlotPostTemplate.addElement("</td>");
        Result._NoItemsInContainerTemplate = VectorUtils.InitWithElement("<tr><td></td><td><li>${ContainerSlotsCount} empty ${ContainerSlotsSize} slots</td><td></td></li></tr>");
        Result._ContainedEmptySlotTemplate = VectorUtils.InitWithElement("<td></td><td><li>empty ${ContainerSlotsSize} slot</li></td><td></td>");
        Result._ContainedFilledSlotTemplate = VectorUtils.InitWithElement("<td></td><td><li><a href='" + Application.BrowserItemByIdUrlPrefix() + "${Id}'>${Name} (# ${Id})</a></li></td>");
        Result._ContainedFilledStackTemplate = VectorUtils.InitWithElement("<td></td><td><li><a href='" + Application.BrowserItemByIdUrlPrefix() + "${Id}'>${Amount} x ${Name} (# ${Id})</a></li></td>");
        Result._ContainedNonTransferableSlotTemplate = new Vector<String>();
        Result._ContainedNonTransferableSlotTemplate.addElement("<td nowrap='1'>");
        Result._ContainedNonTransferableSlotTemplate.addElement("<input type='hidden' value='${Id}' name='left-status-bag-slot-${SlotNo}-${SlotInBagNo}-id'/>");
        Result._ContainedNonTransferableSlotTemplate.addElement("<input type='hidden' value='${Amount}' name='left-status-bag-slot-${SlotNo}-${SlotInBagNo}-amount'/>");
        Result._ContainedNonTransferableSlotTemplate.addElement("</td>");
        Result._ContainedTransferableSlotTemplate = new Vector<String>();
        Result._ContainedTransferableSlotTemplate.addElement("<td nowrap='1'>");
        Result._ContainedTransferableSlotTemplate.addElement("<input type='checkbox' name='left-transfer-bag-slot-${SlotNo}-${SlotInBagNo}' />");
        Result._ContainedTransferableSlotTemplate.addElement("<input type='text' value='${Amount}' size='4' name='left-transfer-bag-slot-${SlotNo}-${SlotInBagNo}-amount-to-transfer' class='form'/><b>&nbsp;&gt;&gt;&gt;</b>");
        Result._ContainedTransferableSlotTemplate.addElement("<input type='hidden' value='${Id}' name='left-status-bag-slot-${SlotNo}-${SlotInBagNo}-id'/>");
        Result._ContainedTransferableSlotTemplate.addElement("<input type='hidden' value='${Amount}' name='left-status-bag-slot-${SlotNo}-${SlotInBagNo}-amount'/>");
        Result._ContainedTransferableSlotTemplate.addElement("</td>");
        return Result;
    }

    public static InventoryRenderTemplate RightSideWarehouseTemplate(WebApplication Application) {
        InventoryRenderTemplate Result;
        Result = new InventoryRenderTemplate();
        Result._OnlyTransferMarketable = false;
        Result._ContainerSeparatorLine = new Vector<String>();
        Result._TopLevelEmptySlotTemplate = VectorUtils.InitWithElement("<td colspan='2'><li>empty " + ItemSize.GIANT + " slot</li></td><td></td>");
        Result._TopLevelFilledSlotTemplate = VectorUtils.InitWithElement("<td colspan='2'><li><b><a href='" + Application.BrowserItemByIdUrlPrefix() + "${Id}'>${Amount} x ${Name} (# ${Id})</a></b></li></td>");
        Result._TopLevelFilledStackTemplate = VectorUtils.InitWithElement("<td colspan='2'><li><b><a href='" + Application.BrowserItemByIdUrlPrefix() + "${Id}'>${Amount} x ${Name} (# ${Id})</a></b></li></td>");
        Result._TopLevelNonTransferableSlotPreTemplate = VectorUtils.InitWithElement("<td nowrap='1'></td>");
        Result._TopLevelTransferableSlotPreTemplate = new Vector<String>();
        Result._TopLevelTransferableSlotPreTemplate.addElement("<td nowrap='1'>");
        Result._TopLevelTransferableSlotPreTemplate.addElement("<b>&lt;&lt;&lt;&nbsp;</b><input type='checkbox' name='right-transfer-id-${Id}' />");
        Result._TopLevelTransferableSlotPreTemplate.addElement("<input type='text' value='${Amount}' size='4' name='right-transfer-id-${Id}-amount-to-transfer' class='form'/>&nbsp;");
        Result._TopLevelTransferableSlotPreTemplate.addElement("</td>");
        Result._TopLevelNonTransferableSlotPostTemplate = new Vector<String>();
        Result._TopLevelTransferableSlotPostTemplate = new Vector<String>();
        Result._NoItemsInContainerTemplate = new Vector<String>();
        Result._ContainedEmptySlotTemplate = new Vector<String>();
        Result._ContainedFilledSlotTemplate = new Vector<String>();
        Result._ContainedFilledStackTemplate = new Vector<String>();
        Result._ContainedNonTransferableSlotTemplate = new Vector<String>();
        Result._ContainedTransferableSlotTemplate = new Vector<String>();
        return Result;
    }

    public boolean OnlyTransferMarketable() {
        boolean Result = _OnlyTransferMarketable;
        return Result;
    }

    public Vector<String> ContainerSeparatorLine() {
        Vector<String> Result = _ContainerSeparatorLine;
        return Result;
    }

    public Vector<String> TopLevelEmptySlotTemplate() {
        Vector<String> Result = _TopLevelEmptySlotTemplate;
        return Result;
    }

    public Vector<String> TopLevelFilledSlotTemplate() {
        Vector<String> Result = _TopLevelFilledSlotTemplate;
        return Result;
    }

    public Vector<String> TopLevelFilledStackTemplate() {
        Vector<String> Result = _TopLevelFilledStackTemplate;
        return Result;
    }

    public Vector<String> TopLevelNonTransferableSlotPreTemplate() {
        Vector<String> Result = _TopLevelNonTransferableSlotPreTemplate;
        return Result;
    }

    public Vector<String> TopLevelTransferableSlotPreTemplate() {
        Vector<String> Result = _TopLevelTransferableSlotPreTemplate;
        return Result;
    }

    public Vector<String> TopLevelNonTransferableSlotPostTemplate() {
        Vector<String> Result = _TopLevelNonTransferableSlotPostTemplate;
        return Result;
    }

    public Vector<String> TopLevelTransferableSlotPostTemplate() {
        Vector<String> Result = _TopLevelTransferableSlotPostTemplate;
        return Result;
    }

    public Vector<String> NoItemsInContainerTemplate() {
        Vector<String> Result = _NoItemsInContainerTemplate;
        return Result;
    }

    public Vector<String> ContainedEmptySlotTemplate() {
        Vector<String> Result = _ContainedEmptySlotTemplate;
        return Result;
    }

    public Vector<String> ContainedFilledSlotTemplate() {
        Vector<String> Result = _ContainedFilledSlotTemplate;
        return Result;
    }

    public Vector<String> ContainedFilledStackTemplate() {
        Vector<String> Result = _ContainedFilledStackTemplate;
        return Result;
    }

    public Vector<String> ContainedNonTransferableSlotTemplate() {
        Vector<String> Result = _ContainedNonTransferableSlotTemplate;
        return Result;
    }

    public Vector<String> ContainedTransferableSlotTemplate() {
        Vector<String> Result = _ContainedTransferableSlotTemplate;
        return Result;
    }

    protected boolean _OnlyTransferMarketable;

    protected Vector<String> _ContainerSeparatorLine;

    protected Vector<String> _TopLevelEmptySlotTemplate;

    protected Vector<String> _TopLevelFilledSlotTemplate;

    protected Vector<String> _TopLevelFilledStackTemplate;

    protected Vector<String> _TopLevelNonTransferableSlotPreTemplate;

    protected Vector<String> _TopLevelTransferableSlotPreTemplate;

    protected Vector<String> _TopLevelNonTransferableSlotPostTemplate;

    protected Vector<String> _TopLevelTransferableSlotPostTemplate;

    protected Vector<String> _NoItemsInContainerTemplate;

    protected Vector<String> _ContainedEmptySlotTemplate;

    protected Vector<String> _ContainedFilledSlotTemplate;

    protected Vector<String> _ContainedFilledStackTemplate;

    protected Vector<String> _ContainedNonTransferableSlotTemplate;

    protected Vector<String> _ContainedTransferableSlotTemplate;

    protected InventoryRenderTemplate() {
    }
}
