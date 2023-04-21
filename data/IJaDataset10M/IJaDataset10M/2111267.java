package jpatch.boundary;

import javax.swing.*;
import jpatch.boundary.action.*;

public class JPatchPopupMenu extends JPopupMenu {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JMenu menuShow;

    private JMenu menuView;

    private JMenu menuZBuffer;

    private JMenu menuLight;

    private JMenu menuRotoscope;

    private JMenu menuSelection;

    private JMenu menuTangents;

    private JMenu menuTools;

    private JCheckBoxMenuItem miPoints;

    private JCheckBoxMenuItem miCurves;

    private JCheckBoxMenuItem miPatches;

    private JCheckBoxMenuItem miRotoscope;

    private JCheckBoxMenuItem miBezier;

    private JCheckBoxMenuItem miZBuffer;

    private JCheckBoxMenuItem miLightOff;

    private JCheckBoxMenuItem miLightSimple;

    private JCheckBoxMenuItem miLightHead;

    private JCheckBoxMenuItem miLightThreePoint;

    private JCheckBoxMenuItem miLightSticky;

    private JMenuItem miSetRotoscope;

    private JMenuItem miClearRotoscope;

    public JPatchPopupMenu(ViewDefinition viewDefinition) {
        miPoints = new JCheckBoxMenuItem(new ShowPointsAction(viewDefinition));
        miCurves = new JCheckBoxMenuItem(new ShowCurvesAction(viewDefinition));
        miPatches = new JCheckBoxMenuItem(new ShowPatchesAction(viewDefinition));
        miBezier = new JCheckBoxMenuItem(new ShowBezierAction(viewDefinition));
        miRotoscope = new JCheckBoxMenuItem(new ShowRotoscopeAction(viewDefinition));
        miZBuffer = new JCheckBoxMenuItem(new AlwaysUseZBufferAction(viewDefinition));
        miLightOff = new JCheckBoxMenuItem(new LightingOffAction(MainFrame.getInstance().getJPatchScreen()));
        miLightSimple = new JCheckBoxMenuItem(new LightingSimpleAction(MainFrame.getInstance().getJPatchScreen()));
        miLightHead = new JCheckBoxMenuItem(new LightingHeadAction(MainFrame.getInstance().getJPatchScreen()));
        miLightThreePoint = new JCheckBoxMenuItem(new LightingThreePointAction(MainFrame.getInstance().getJPatchScreen()));
        miLightSticky = new JCheckBoxMenuItem(new LightingStickyAction(MainFrame.getInstance().getJPatchScreen()));
        miPoints.setState(viewDefinition.renderPoints());
        miCurves.setState(viewDefinition.renderCurves());
        miPatches.setState(viewDefinition.renderPatches());
        miRotoscope.setState(viewDefinition.showRotoscope());
        miBezier.setState(viewDefinition.renderBezierCPs());
        miZBuffer.setState(viewDefinition.alwaysUseZBuffer());
        miLightOff.setState(MainFrame.getInstance().getJPatchScreen().getLightingMode() == JPatchScreen.LIGHT_OFF);
        miLightSimple.setState(MainFrame.getInstance().getJPatchScreen().getLightingMode() == JPatchScreen.LIGHT_SIMPLE);
        miLightHead.setState(MainFrame.getInstance().getJPatchScreen().getLightingMode() == JPatchScreen.LIGHT_HEAD);
        miLightThreePoint.setState(MainFrame.getInstance().getJPatchScreen().getLightingMode() == JPatchScreen.LIGHT_THREE_POINT);
        miLightSticky.setState(MainFrame.getInstance().getJPatchScreen().isStickyLight());
        menuShow = new JMenu("show");
        menuShow.add(miPoints);
        menuShow.add(miCurves);
        menuShow.add(miPatches);
        menuShow.add(miRotoscope);
        menuView = new JMenu("view");
        for (int i = ViewDefinition.FRONT; i <= ViewDefinition.BIRDS_EYE; i++) {
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem(new ViewAction(viewDefinition, i));
            if (viewDefinition.getView() == i) mi.setSelected(true);
            menuView.add(mi);
        }
        menuZBuffer = new JMenu("z-buffer renderer");
        menuLight = new JMenu("light");
        menuLight.add(miLightOff);
        menuLight.add(miLightSimple);
        menuLight.add(miLightHead);
        menuLight.add(miLightThreePoint);
        menuLight.addSeparator();
        menuLight.add(miLightSticky);
        menuZBuffer.add(miZBuffer);
        menuZBuffer.add(menuLight);
        menuZBuffer.add(new ZBufferQualityAction());
        menuRotoscope = new JMenu("rotoscope");
        miSetRotoscope = new JMenuItem(new SetRotoscopeAction(viewDefinition));
        miClearRotoscope = new JMenuItem(new ClearRotoscopeAction(viewDefinition));
        menuRotoscope.add(miSetRotoscope);
        menuRotoscope.add(miClearRotoscope);
        if (viewDefinition.getView() == ViewDefinition.BIRDS_EYE) {
            miSetRotoscope.setEnabled(false);
        }
        if (MainFrame.getInstance().getModel().getRotoscope(viewDefinition.getView()) == null) {
            miClearRotoscope.setEnabled(false);
        }
        menuSelection = new JMenu("selection");
        menuTools = new JMenu("tools");
        menuTangents = new JMenu("tangents");
        JMenuItem miSelectNone = new JMenuItem(new SelectNoneAction());
        JMenuItem miExtendSelection = new JMenuItem(new ExtendSelectionAction());
        JMenuItem miInvertSelection = new JMenuItem(new InvertSelectionAction());
        JMenuItem miUnlock = new JMenuItem(new ClearViewLockAction(viewDefinition));
        miUnlock.setEnabled(viewDefinition.isLocked());
        JMenuItem miLock = new JMenuItem(new SetViewLockAction(viewDefinition));
        JMenuItem miNextCurve = new JMenuItem(new NextCurveAction());
        JMenuItem miAddStubs = new JMenuItem(new AddStubsAction());
        JMenuItem miRemoveStubs = new JMenuItem(new RemoveStubsAction());
        if (MainFrame.getInstance().getSelection() == null) {
            miSelectNone.setEnabled(false);
            miExtendSelection.setEnabled(false);
            miInvertSelection.setEnabled(false);
            menuTools.setEnabled(false);
            menuTangents.setEnabled(false);
            miLock.setEnabled(false);
            miNextCurve.setEnabled(false);
            miAddStubs.setEnabled(false);
            miRemoveStubs.setEnabled(false);
        } else {
            if (MainFrame.getInstance().getSelection().getDirection() != 0 && !MainFrame.getInstance().getSelection().isSingle()) miNextCurve.setEnabled(false);
        }
        menuSelection.add(miSelectNone);
        menuSelection.add(new SelectAllAction());
        menuSelection.add(miInvertSelection);
        menuSelection.add(miExtendSelection);
        menuSelection.add(miNextCurve);
        menuTangents.add(new ChangeTangentModeAction(ChangeTangentModeAction.PEAK));
        menuTangents.add(new ChangeTangentModeAction(ChangeTangentModeAction.JPATCH));
        menuTangents.add(new ChangeTangentModeAction(ChangeTangentModeAction.SPATCH));
        JMenu menuFlip = new JMenu("flip");
        menuFlip.add(new FlipAction(FlipAction.X));
        menuFlip.add(new FlipAction(FlipAction.Y));
        menuFlip.add(new FlipAction(FlipAction.Z));
        menuTools.add(menuFlip);
        menuTools.add(new FlipPatchesAction());
        menuTools.add(new AlignPatchesAction());
        menuTools.add(new AlignAction());
        menuTools.add(new AutoMirrorAction());
        menuTools.add(miAddStubs);
        menuTools.add(miRemoveStubs);
        menuTools.add(menuTangents);
        add(menuShow);
        add(menuView);
        add(menuRotoscope);
        if (miUnlock.isEnabled()) add(miUnlock); else add(miLock);
        addSeparator();
        add(menuSelection);
        add(menuTools);
    }
}
