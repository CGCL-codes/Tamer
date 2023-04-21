package org.proteomecommons.MSExpedite.app;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author takis
 */
public class MenuFactory {

    public static JMenu createEffectsMenu(ISessionHandler h) {
        JMenu menu = new JMenu("Effects");
        menu.add(ButtonFactory.createPlayMenuItem(h));
        menu.add(ButtonFactory.createPauseMenuItem(h));
        menu.add(ButtonFactory.createStepForwardMenuItem(h));
        menu.add(ButtonFactory.createStepBackwardsMenuItem(h));
        return menu;
    }

    public static JMenu createBannerMenu(ISessionHandler h) {
        JMenu bannerMenu = new JMenu("Banner");
        JCheckBoxMenuItem editMenuItem = ButtonFactory.createEditBannerMenuItem(h, false);
        JCheckBoxMenuItem showHideBannerMenuItem = ButtonFactory.createHideBannerMenuItem(h, true);
        bannerMenu.add(showHideBannerMenuItem);
        bannerMenu.add(editMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.Banner.class, editMenuItem);
        return bannerMenu;
    }

    public static JMenu createBannerMenu(ISessionHandler h, IContext context) {
        JMenu bannerMenu = new JMenu("Banner");
        JCheckBoxMenuItem editMenuItem = ButtonFactory.createEditBannerMenuItem(h, context, false);
        JCheckBoxMenuItem showHideBannerMenuItem = ButtonFactory.createHideBannerMenuItem(h, context, true);
        bannerMenu.add(showHideBannerMenuItem);
        bannerMenu.add(editMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.Banner.class, editMenuItem);
        return bannerMenu;
    }

    public static JMenu createViewMenu(ISessionHandler h, AppAttrHandler ahl) {
        JMenu viewMenu = new JMenu("View");
        JCheckBoxMenuItem viewYBPairsMenuItem = ButtonFactory.createYBPairMenuItem(h, false);
        JCheckBoxMenuItem viewAutoAnnotMenuItem = ButtonFactory.createAutomatedAnnotationMenuItem(h, false);
        viewMenu.add(viewAutoAnnotMenuItem);
        viewMenu.add(viewYBPairsMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.app.YBPairPanelUtility.class, viewYBPairsMenuItem);
        return viewMenu;
    }

    public static JMenu createViewAnnotationMenu(ISessionHandler h, AppAttrHandler ahl) {
        JMenu viewAnnotationMenu = new JMenu("Annotation");
        JCheckBoxMenuItem viewAssignmentMenuItem = ButtonFactory.createViewAnnotAssignmentMenuItem(h, ahl);
        JCheckBoxMenuItem viewObservedAnnotMassesMenuItem = ButtonFactory.createViewAnnotObservedMassesMenuItem(h, ahl);
        JCheckBoxMenuItem viewTheoreticalAnnotMassesMenuItem = ButtonFactory.createViewAnnotTheoreticalMassesMenuItem(h, ahl);
        JCheckBoxMenuItem viewMassDiffMenuItem = ButtonFactory.createViewMassDiffMenuItem(h, ahl);
        JCheckBoxMenuItem viewAnnotPPMMenuItem = ButtonFactory.createViewAnnotPPMMenuItem(h, ahl);
        JMenu viewCursorAnnotationMenu = createViewCursorAnnotationMenu(h, ahl);
        viewAnnotationMenu.add(viewAssignmentMenuItem);
        viewAnnotationMenu.add(viewMassDiffMenuItem);
        viewAnnotationMenu.add(viewObservedAnnotMassesMenuItem);
        viewAnnotationMenu.add(viewTheoreticalAnnotMassesMenuItem);
        viewAnnotationMenu.add(viewAnnotPPMMenuItem);
        viewAnnotationMenu.add(viewCursorAnnotationMenu);
        return viewAnnotationMenu;
    }

    public static JMenu createViewCursorAnnotationMenu(ISessionHandler h, AppAttrHandler ahl) {
        JMenu cursorAnnotMenu = new JMenu("Cursor");
        JCheckBoxMenuItem viewAssignmentMenuItem = ButtonFactory.createViewCursorAnnotAssignmentMenuItem(h, ahl);
        JCheckBoxMenuItem viewObservedAnnotMassesMenuItem = ButtonFactory.createViewCursorAnnotObservedMassesMenuItem(h, ahl);
        JCheckBoxMenuItem viewTheoreticalAnnotMassesMenuItem = ButtonFactory.createViewCursorAnnotTheoreticalMassesMenuItem(h, ahl);
        JCheckBoxMenuItem viewAnnotPPMMenuItem = ButtonFactory.createViewCursorAnnotPPMMenuItem(h, ahl);
        JCheckBoxMenuItem viewMassDiffMenuItem = ButtonFactory.createViewCursorMassDiffMenuItem(h, ahl);
        cursorAnnotMenu.add(viewAssignmentMenuItem);
        cursorAnnotMenu.add(viewMassDiffMenuItem);
        cursorAnnotMenu.add(viewObservedAnnotMassesMenuItem);
        cursorAnnotMenu.add(viewTheoreticalAnnotMassesMenuItem);
        cursorAnnotMenu.add(viewAnnotPPMMenuItem);
        return cursorAnnotMenu;
    }

    public static JMenu createAnnotationMenu(ISessionHandler h, IContext context) {
        JMenu annotationMenu = new JMenu("Annotation");
        JCheckBoxMenuItem manualAnnotationMenuItem = ButtonFactory.createManualAnnotationMenuItem(h, context, false);
        JCheckBoxMenuItem assistedAnnotationMenuItem = ButtonFactory.createAssistedAnnotationMenuItem(h, context, false);
        JCheckBoxMenuItem editAnnotationMenuItem = ButtonFactory.createEditAnnotationMenuItem(h, context, false);
        JMenuItem clearAnnotationMenuItem = ButtonFactory.createClearAnnotationMenuItem(h);
        annotationMenu.add(manualAnnotationMenuItem);
        annotationMenu.add(assistedAnnotationMenuItem);
        annotationMenu.add(editAnnotationMenuItem);
        annotationMenu.add(clearAnnotationMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.AnnotationGraph.class, manualAnnotationMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.AnnotationGraph.class, assistedAnnotationMenuItem);
        return annotationMenu;
    }

    public static JMenu createAnnotationMenu(ISessionHandler h) {
        JMenu annotationMenu = new JMenu("Annotation");
        JCheckBoxMenuItem manualAnnotationMenuItem = ButtonFactory.createManualAnnotationMenuItem(h, false);
        JCheckBoxMenuItem assistedAnnotationMenuItem = ButtonFactory.createAssistedAnnotationMenuItem(h, false);
        JCheckBoxMenuItem editAnnotationMenuItem = ButtonFactory.createEditAnnotationMenuItem(h, false);
        JMenuItem clearAnnotationMenuItem = ButtonFactory.createClearAnnotationMenuItem(h);
        annotationMenu.add(manualAnnotationMenuItem);
        annotationMenu.add(assistedAnnotationMenuItem);
        annotationMenu.add(editAnnotationMenuItem);
        annotationMenu.add(clearAnnotationMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.AnnotationGraph.class, manualAnnotationMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.AnnotationGraph.class, assistedAnnotationMenuItem);
        return annotationMenu;
    }

    public static JMenu createPeaksMenu(ISessionHandler h) {
        JMenu peaksMenu = new JMenu("Peaks");
        JCheckBoxMenuItem deletePeaksMenuItem = ButtonFactory.createDeletePeaksMenuItem(h, false);
        JCheckBoxMenuItem manualLabelPeaksMenuItem = ButtonFactory.createManuallyLabelPeaksMenuItem(h, false);
        JCheckBoxMenuItem hidePeaksMenuItem = ButtonFactory.createHidePeaksMenuItem(h, false);
        peaksMenu.add(manualLabelPeaksMenuItem);
        peaksMenu.add(new JSeparator());
        peaksMenu.add(deletePeaksMenuItem);
        peaksMenu.add(new JSeparator());
        peaksMenu.add(hidePeaksMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.MPPController.class, manualLabelPeaksMenuItem);
        UIMouseActionManager.associate(org.proteomecommons.MSExpedite.Graph.RemovePeaksController.class, deletePeaksMenuItem);
        return peaksMenu;
    }

    public static JMenu createSmoothingMenu(ISessionHandler h) {
        JMenu smoothMenu = new JMenu("Smoothing");
        JMenuItem gaussianMenuItem = ButtonFactory.createGaussianMenuItem(h);
        JMenuItem lorentzianMenuItem = ButtonFactory.createLorentzianMenuItem(h);
        JMenuItem gaussLorentzMenutItem = ButtonFactory.createGaussianLorentzianActionListener(h);
        smoothMenu.add(gaussianMenuItem);
        smoothMenu.add(lorentzianMenuItem);
        smoothMenu.add(gaussLorentzMenutItem);
        return smoothMenu;
    }
}
