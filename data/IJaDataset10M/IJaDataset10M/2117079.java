package org.kalypso.nofdpidss.evaluation.assessment.view.edit.detailtable.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.contribs.eclipse.ui.controls.ScrolledSection;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.MyColors;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICriterion;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICriterionDefinition;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ILocalCriterionMeasureValue;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.utils.gml.ATGmlUtils;
import org.kalypso.nofdpidss.core.common.utils.gml.VMGmlUtils;
import org.kalypso.nofdpidss.core.view.parts.IAbstractMenuPart;
import org.kalypso.nofdpidss.core.view.widgets.MySection;
import org.kalypso.nofdpidss.core.view.widgets.TextBoxComposite;
import org.kalypso.nofdpidss.evaluation.assessment.view.edit.detailtable.ATCriterionData;
import org.kalypso.nofdpidss.evaluation.assessment.view.edit.detailtable.ATDetailTableMapping;
import org.kalypso.nofdpidss.evaluation.assessment.view.edit.mappingtable.boxes.ATBoxDefinition;
import org.kalypso.nofdpidss.evaluation.i18n.Messages;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class ATDetailTableWidgetGetter {

    private static final int TEXT_BOX_WIDTH = 80;

    private static final int TEXT_BOX_SPACE = 30;

    private static final int COLOR_LABEL_WIDTH = 15;

    private static final int ROW_HEADER_WIDTH = 200;

    public static void getCard(final IAbstractMenuPart part, final FormToolkit toolkit, final ATDetailTableMapping tableMap, final Feature fVariant, final ATBoxDefinition[] arrDefinition, final Composite cParent, final String sectionId) {
        final Map<Integer, String> hColMeasureMapping = ATDetailTableWidgetGetter.getColumnMeasureMapping(fVariant, tableMap);
        final String variantName = Messages.ATDetailTableWidgetGetter_0 + " " + FeatureUtils.getFeatureName(GmlConstants.NS_VARIANTS, fVariant);
        final MySection section = new MySection(part, toolkit, cParent, ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR, false);
        final Composite secBody = section.setup(variantName, new GridData(GridData.FILL, GridData.BEGINNING, true, false), new GridData(GridData.FILL, GridData.BEGINNING, true, false), sectionId);
        final GridLayout secLayout = new GridLayout(tableMap.getNumberOfMeasureColumns() + tableMap.getNumberOfMeasureColumns() - 1 + 4, false);
        secLayout.verticalSpacing = secLayout.horizontalSpacing = secLayout.marginWidth = secLayout.marginLeft = secLayout.marginRight = secLayout.marginTop = secLayout.marginBottom = 0;
        secBody.setLayout(secLayout);
        final int[] boxOrder = { 0, 3, 1, 4, 2, 5 };
        final Color[] colors = { MyColors.cMiddleOcher, MyColors.cMiddleBlue, MyColors.cMiddleGreen, MyColors.cDarkOcher, MyColors.cDarkBlue, MyColors.cDarkGreen };
        ATDetailTableWidgetGetter.getCardHeader(toolkit, secBody, tableMap, hColMeasureMapping);
        int countColorizeRow = 0;
        for (final int index : boxOrder) {
            if (arrDefinition[index] == null) return;
            final List<String> lstCriteria = arrDefinition[index].getCriteria();
            for (final String crDefId : lstCriteria) countColorizeRow = ATDetailTableWidgetGetter.getCardRow(tableMap, fVariant, crDefId, toolkit, section, secBody, colors, hColMeasureMapping, index, countColorizeRow);
        }
    }

    private static void getCardHeader(final FormToolkit toolkit, final Composite secBody, final ATDetailTableMapping map, final Map<Integer, String> hColMeasureMapping) {
        final Composite cCr = toolkit.createComposite(secBody);
        final GridLayout dCr = new GridLayout();
        dCr.verticalSpacing = dCr.horizontalSpacing = dCr.marginWidth = dCr.marginLeft = dCr.marginRight = dCr.marginTop = dCr.marginBottom = 0;
        cCr.setLayout(dCr);
        final GridData dataCr = new GridData(GridData.FILL, GridData.FILL, false, true, 2, 0);
        dataCr.minimumWidth = dataCr.widthHint = ATDetailTableWidgetGetter.ROW_HEADER_WIDTH + ATDetailTableWidgetGetter.COLOR_LABEL_WIDTH + 10;
        cCr.setLayoutData(dataCr);
        cCr.setBackground(MyColors.cLightGrey2);
        final Label lCr = toolkit.createLabel(cCr, Messages.ATDetailTableWidgetGetter_1);
        lCr.setLayoutData(new GridData(GridData.FILL, GridData.END, true, true));
        lCr.setFont(MyColors.fTableHeader);
        lCr.setBackground(MyColors.cLightGrey2);
        final Composite cValue = toolkit.createComposite(secBody);
        final GridLayout lValue = new GridLayout();
        lValue.verticalSpacing = lValue.horizontalSpacing = lValue.marginWidth = lValue.marginLeft = lValue.marginRight = lValue.marginTop = lValue.marginBottom = 0;
        cValue.setLayout(lValue);
        final GridData dValue = new GridData(GridData.FILL, GridData.FILL, false, true);
        dValue.minimumWidth = dValue.widthHint = ATDetailTableWidgetGetter.TEXT_BOX_WIDTH + 20;
        cValue.setLayoutData(dValue);
        cValue.setBackground(MyColors.cLightGrey2);
        final Label lbValue = toolkit.createLabel(cValue, Messages.ATDetailTableWidgetGetter_2);
        lbValue.setLayoutData(new GridData(GridData.FILL, GridData.END, true, true));
        lbValue.setFont(MyColors.fTableHeader);
        lbValue.setBackground(MyColors.cLightGrey2);
        final Composite cMean = toolkit.createComposite(secBody);
        final GridLayout lMean = new GridLayout();
        lMean.verticalSpacing = lMean.horizontalSpacing = lMean.marginWidth = lMean.marginLeft = lMean.marginRight = lMean.marginTop = lMean.marginBottom = 0;
        cMean.setLayout(lMean);
        final GridData dMean = new GridData(GridData.FILL, GridData.FILL, false, true);
        dMean.widthHint = dMean.minimumWidth = ATDetailTableWidgetGetter.TEXT_BOX_WIDTH + 20;
        cMean.setLayoutData(dMean);
        cMean.setBackground(MyColors.cLightGrey2);
        final Label lSum = toolkit.createLabel(cMean, Messages.ATDetailTableWidgetGetter_3);
        lSum.setLayoutData(new GridData(GridData.CENTER, GridData.END, true, true));
        lSum.setFont(MyColors.fTableHeader);
        lSum.setBackground(MyColors.cLightGrey2);
        for (int i = 0; i < map.getNumberOfMeasureColumns(); i++) {
            final Feature fMeasure = VMGmlUtils.getMeasureFeature(hColMeasureMapping.get(i));
            final Composite cMeasure = toolkit.createComposite(secBody);
            final GridLayout lMeasure = new GridLayout();
            lMeasure.verticalSpacing = lMeasure.horizontalSpacing = lMeasure.marginWidth = lMeasure.marginLeft = lMeasure.marginRight = lMeasure.marginTop = lMeasure.marginBottom = 0;
            cMeasure.setLayout(lMeasure);
            final GridData myData = new GridData(GridData.FILL, GridData.FILL, true, true);
            myData.widthHint = myData.minimumWidth = ATDetailTableWidgetGetter.TEXT_BOX_WIDTH;
            cMeasure.setLayoutData(myData);
            cMeasure.setBackground(MyColors.cLightGrey2);
            final FormText fText = toolkit.createFormText(cMeasure, false);
            if (fMeasure != null) {
                final Object prop = fMeasure.getProperty(GmlConstants.QN_MEASURE_NAME);
                fText.setText("<p>" + (String) prop + "</p>", true, false);
            }
            final GridData data = new GridData(GridData.FILL, GridData.END, true, true);
            data.widthHint = 50;
            fText.setLayoutData(data);
            fText.setWhitespaceNormalized(true);
            fText.setBackground(MyColors.cLightGrey2);
            fText.setFont(MyColors.fTableHeader);
            if (i < map.getNumberOfMeasureColumns() - 1) {
                final Label lSpacer = toolkit.createLabel(secBody, "");
                final GridData sData = new GridData(GridData.FILL, GridData.FILL, true, false);
                sData.widthHint = sData.minimumWidth = 20;
                lSpacer.setLayoutData(sData);
                lSpacer.setBackground(MyColors.cLightGrey2);
            }
        }
    }

    private static int getCardRow(final ATDetailTableMapping tableMap, final Feature fVariant, final String sCrDefId, final FormToolkit toolkit, final ScrolledSection section, final Composite secBody, final Color[] colors, final Map<Integer, String> hColMeasureMapping, final int index, int countColorizeRow) {
        final Feature fCriterion = ATGmlUtils.getCriterionDefFeature(sCrDefId);
        if (fCriterion == null) return countColorizeRow;
        final Composite cColor = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, 15);
        final Label lColor = toolkit.createLabel(cColor, "");
        final GridData dColor = new GridData(GridData.BEGINNING, GridData.BEGINNING, true, true);
        dColor.minimumHeight = dColor.minimumWidth = dColor.heightHint = dColor.widthHint = ATDetailTableWidgetGetter.COLOR_LABEL_WIDTH;
        lColor.setLayoutData(dColor);
        lColor.setBackground(colors[index]);
        final Object objName = fCriterion.getProperty(GmlConstants.QN_ASSESSMENT_NAME);
        String sName;
        if (objName == null || !(objName instanceof String)) sName = Messages.ATDetailTableWidgetGetter_8; else sName = (String) objName;
        final Object objMea = fCriterion.getProperty(ICriterionDefinition.QN_UNIT);
        String sMea;
        if (objMea == null || objMea.equals("")) sMea = ""; else sMea = (String) objMea;
        final String str = "<p>" + sName + " [" + sMea + "]</p>";
        final Composite cCrName = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, ATDetailTableWidgetGetter.ROW_HEADER_WIDTH);
        final FormText ftCrName = toolkit.createFormText(cCrName, false);
        ftCrName.setText(str, true, false);
        final GridData data = new GridData(GridData.BEGINNING, GridData.BEGINNING, false, true);
        data.minimumWidth = data.widthHint = ATDetailTableWidgetGetter.ROW_HEADER_WIDTH;
        ftCrName.setLayoutData(data);
        final MyBasePool pool = NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final Feature fCrDef = ATGmlUtils.getCriterionDefFeature(sCrDefId);
        if (fCrDef == null) {
            final Label lTemp = toolkit.createLabel(secBody, "ERROR");
            lTemp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 10, 0));
            if (countColorizeRow % 2 != 0) {
                lTemp.setBackground(MyColors.cLightGrey1);
                cColor.setBackground(MyColors.cLightGrey1);
                ftCrName.setBackground(MyColors.cLightGrey1);
                cCrName.setBackground(MyColors.cLightGrey1);
            }
            return ++countColorizeRow;
        }
        Color bgColor = MyColors.cWhite;
        if (countColorizeRow % 2 != 0) bgColor = MyColors.cLightGrey1;
        final Feature fCr = ATGmlUtils.getCriterionFeature(fVariant, fCrDef);
        if (fCr == null) throw new IllegalStateException(Messages.ATDetailTableWidgetGetter_15);
        final GridData tData = new GridData(GridData.FILL, GridData.FILL, false, false);
        tData.minimumWidth = tData.widthHint = ATDetailTableWidgetGetter.TEXT_BOX_WIDTH;
        final TextBoxComposite myTb = new TextBoxComposite(section, pool, fCr, ICriterion.QN_VALUE, Double.valueOf("-1"));
        final Double aggMeasureValue = tableMap.getAggregatedMeasureValue(fVariant, fCrDef);
        final GlobalValueValidator validator = new GlobalValueValidator(fCr, ICriterion.QN_VALUE, aggMeasureValue);
        myTb.addWarningValidator(validator);
        myTb.draw(toolkit, secBody, tData, null, null, SWT.RIGHT | SWT.BORDER, bgColor, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH - ATDetailTableWidgetGetter.TEXT_BOX_SPACE, true);
        final Composite cMean = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH);
        Label lMean;
        if (aggMeasureValue == null) lMean = toolkit.createLabel(cMean, "", SWT.CENTER); else lMean = toolkit.createLabel(cMean, String.format("%6.2f", aggMeasureValue), SWT.CENTER);
        final GridData dMean = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
        dMean.minimumWidth = dMean.widthHint = ATDetailTableWidgetGetter.TEXT_BOX_WIDTH;
        lMean.setLayoutData(dMean);
        ATDetailTableWidgetGetter.getCardRowMeasures(toolkit, section, secBody, tableMap, hColMeasureMapping, fCrDef, countColorizeRow);
        if (countColorizeRow % 2 != 0) {
            cColor.setBackground(MyColors.cLightGrey1);
            ftCrName.setBackground(MyColors.cLightGrey1);
            cCrName.setBackground(MyColors.cLightGrey1);
            lMean.setBackground(MyColors.cLightGrey1);
            cMean.setBackground(MyColors.cLightGrey1);
        }
        return ++countColorizeRow;
    }

    private static void getCardRowMeasures(final FormToolkit toolkit, final ScrolledSection section, final Composite secBody, final ATDetailTableMapping tableMap, final Map<Integer, String> hColMeasureMapping, final Feature fCrDef, final Integer countColorizeRow) {
        if (ATCriterionData.TYPE.LOCAL.equals(tableMap.getTypeOfCriterion(fCrDef))) {
            for (int i = 0; i < tableMap.getNumberOfMeasureColumns(); i++) if (hColMeasureMapping.containsKey(i)) {
                final MyBasePool pool = NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
                final String sMeasureId = hColMeasureMapping.get(i);
                final Feature fMeasureValue = ATGmlUtils.getLocalMeasure(sMeasureId, fCrDef);
                final GridData tData = new GridData(GridData.FILL, GridData.FILL, false, false);
                tData.minimumWidth = tData.widthHint = ATDetailTableWidgetGetter.TEXT_BOX_WIDTH;
                Color bgColor = MyColors.cWhite;
                if (countColorizeRow % 2 != 0) bgColor = MyColors.cLightGrey1;
                final TextBoxComposite myTb = new TextBoxComposite(section, pool, fMeasureValue, ILocalCriterionMeasureValue.QN_VALUE, Double.valueOf("-1"));
                final LocalValueValidator validator = new LocalValueValidator(fMeasureValue, ILocalCriterionMeasureValue.QN_VALUE);
                myTb.addWarningValidator(validator);
                myTb.draw(toolkit, secBody, tData, null, null, SWT.RIGHT | SWT.BORDER, bgColor, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH - ATDetailTableWidgetGetter.TEXT_BOX_SPACE, true);
                if (tableMap.getNumberOfMeasureColumns() > i + 1) {
                    final Composite cSpacer = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH / 2);
                    toolkit.createLabel(cSpacer, "");
                    if (countColorizeRow % 2 != 0) cSpacer.setBackground(MyColors.cLightGrey1);
                }
            } else {
                final Composite cSpacer = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH / 2);
                toolkit.createLabel(cSpacer, "");
                if (tableMap.getNumberOfMeasureColumns() > i + 1) {
                    final Composite cSpacer2 = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH / 2);
                    toolkit.createLabel(cSpacer2, "");
                    if (countColorizeRow % 2 != 0) cSpacer2.setBackground(MyColors.cLightGrey1);
                }
                if (countColorizeRow % 2 != 0) cSpacer.setBackground(MyColors.cLightGrey1);
            }
        } else {
            final Composite cSpacer = ATDetailTableWidgetGetter.getOuterComposite(toolkit, secBody, ATDetailTableWidgetGetter.TEXT_BOX_WIDTH / 2);
            toolkit.createLabel(cSpacer, "");
            final Object layoutData = cSpacer.getLayoutData();
            if (layoutData instanceof GridData) {
                final GridData data = (GridData) layoutData;
                data.horizontalSpan = tableMap.getNumberOfMeasureColumns() + tableMap.getNumberOfMeasureColumns() - 1;
            }
            if (countColorizeRow % 2 != 0) cSpacer.setBackground(MyColors.cLightGrey1);
        }
    }

    private static Map<Integer, String> getColumnMeasureMapping(final Feature fVariant, final ATDetailTableMapping tableMap) {
        final Object objLstMeasures = fVariant.getProperty(IVariant.QN_MEASURES);
        if (!(objLstMeasures instanceof List)) new HashMap<Integer, String>();
        final List<String> myMeasures = new ArrayList<String>();
        final List lMeasures = (List) objLstMeasures;
        if (lMeasures == null) return null;
        for (final Object object : lMeasures) {
            if (object == null) continue;
            myMeasures.add((String) object);
        }
        return tableMap.getColMeasureMapping(myMeasures);
    }

    private static Composite getOuterComposite(final FormToolkit toolkit, final Composite secBody, final int width) {
        final Composite composite = toolkit.createComposite(secBody);
        final GridLayout layout = new GridLayout();
        layout.horizontalSpacing = layout.verticalSpacing = layout.marginBottom = layout.marginHeight = layout.marginLeft = layout.marginRight = layout.marginTop = layout.marginWidth = 0;
        composite.setLayout(layout);
        final GridData data = new GridData(GridData.FILL, GridData.FILL, false, false);
        data.minimumWidth = data.widthHint = width;
        composite.setLayoutData(data);
        return composite;
    }
}
