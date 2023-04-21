package jsesh.mdc.interfaces;

import jsesh.mdc.constants.ToggleType;

/**
 * A utility implementation of MDCBuilder.
 * <p>This class can be used as a base for writing simple
 * builders ; however you should take note that its methods basically
 * do nothing. In most cases, you should completely write the builder,
 * in order to forget no construct. The only case where this class is useful is when
 * you are only interested in one construct or two.  
 * @author rosmord
 */
public abstract class MDCBuilderAdapter implements MDCBuilder {

    public void addCadratToBasicItemList(BasicItemListInterface l, CadratInterface c, int shading) {
    }

    public void addCadratToTopItemList(TopItemListInterface l, CadratInterface e, int shading) {
    }

    public void addCartoucheToTopItemList(TopItemListInterface l, CartoucheInterface c) {
    }

    public void addHRuleToTopItemList(TopItemListInterface l, char lineType, int startPos, int endPos) {
    }

    public void addLineBreakToTopItemList(TopItemListInterface l, int skip) {
    }

    public void addModifierToModifierList(ModifierListInterface mods, String name, Integer value) {
    }

    public void addPageBreakToTopItemList(TopItemListInterface l) {
    }

    public void addStartHieroglyphicTextToTopItemList(TopItemListInterface l) {
    }

    public void addStartHieroglyphicTextToBasicItemList(BasicItemListInterface l) {
    }

    public void addTabStopToTopItemList(TopItemListInterface l, int stopWidth) {
    }

    public void addTextSuperscriptToTopItemList(TopItemListInterface l, String text) {
    }

    public void addTextToBasicItemList(BasicItemListInterface l, char scriptCode, String text) {
    }

    public void addTextToTopItemList(TopItemListInterface l, char scriptCode, String text) {
    }

    public void addToggleToBasicItemList(BasicItemListInterface l, ToggleType toggleCode) {
    }

    public void addToggleToTopItemList(TopItemListInterface l, ToggleType toggle) {
    }

    public void addToHorizontalList(HBoxInterface h, HorizontalListElementInterface elt) {
    }

    public void addToLigature(LigatureInterface i, HieroglyphInterface h) {
    }

    public void addToVerticalList(VBoxInterface l, HBoxInterface h) {
    }

    public BasicItemListInterface buildBasicItemList() {
        return null;
    }

    public CadratInterface buildCadrat(VBoxInterface e) {
        return null;
    }

    public CartoucheInterface buildCartouche(int type, int leftPart, BasicItemListInterface e, int rightPart) {
        return null;
    }

    public HBoxInterface buildHBox() {
        return null;
    }

    public HieroglyphInterface buildHieroglyph(boolean isGrammar, int type, String code, ModifierListInterface m, int isEnd) {
        return null;
    }

    public LigatureInterface buildLigature() {
        return null;
    }

    public MDCFileInterface buildMDCFileInterface(TopItemListInterface l) {
        return null;
    }

    public ModifierListInterface buildModifierList() {
        return null;
    }

    public OverwriteInterface buildOverwrite(HieroglyphInterface e1, HieroglyphInterface e2) {
        return null;
    }

    public PhilologyInterface buildPhilology(int code1, BasicItemListInterface e, int code2) {
        return null;
    }

    public SubCadratInterface buildSubCadrat(BasicItemListInterface e) {
        return null;
    }

    public TopItemListInterface buildTopItemList() {
        return null;
    }

    public VBoxInterface buildVBox() {
        return null;
    }

    public void completeLigature(LigatureInterface i) {
    }

    public void reset() {
    }

    public void setHieroglyphPosition(HieroglyphInterface h, int x, int y, int scale) {
    }

    public AbsoluteGroupInterface buildAbsoluteGroup() {
        return null;
    }

    public void addHieroglyphToAbsoluteGroup(AbsoluteGroupInterface RESULT, HieroglyphInterface e) {
    }

    public void addOption(OptionListInterface e1, String optName, int val) {
    }

    public void addOption(OptionListInterface e1, String optName, String val) {
    }

    public void addOption(OptionListInterface e1, String optName) {
    }

    public void addZoneStartToTopItemList(TopItemListInterface e1, ZoneStartInterface e2) {
    }

    public OptionListInterface buildOptionList() {
        return null;
    }

    public ZoneStartInterface buildZone() {
        return null;
    }

    public void setOptionList(ZoneStartInterface result, OptionListInterface e1) {
    }

    public void setOptionList(CadratInterface result, OptionListInterface e1) {
    }

    public ComplexLigatureInterface buildComplexLigature(InnerGroupInterface e1, HieroglyphInterface e2, InnerGroupInterface e3) {
        return null;
    }

    public void addTabbingClearToTopItemList(TopItemListInterface e1) {
    }

    public void addTabbingToTopItemList(TopItemListInterface e1, OptionListInterface e3) {
    }
}
