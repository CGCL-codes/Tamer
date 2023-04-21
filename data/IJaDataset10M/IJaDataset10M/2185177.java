package org.qsari.effectopedia.gui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import org.qsari.effectopedia.base.EffectopediaObject;
import org.qsari.effectopedia.base.ids.ReferenceID;
import org.qsari.effectopedia.base.ids.ReferenceIDs;
import org.qsari.effectopedia.core.objects.Effect_AdverseOutcome;
import org.qsari.effectopedia.core.objects.Effect_DownstreamEffect;
import org.qsari.effectopedia.core.objects.Effect_Endpoint;
import org.qsari.effectopedia.core.objects.Effect_MolecularInitiatingEvent;
import org.qsari.effectopedia.core.objects.Link;
import org.qsari.effectopedia.core.objects.Pathway;
import org.qsari.effectopedia.core.objects.Substance_Chemical;
import org.qsari.effectopedia.core.objects.Substance_ReactiveChemical;
import org.qsari.effectopedia.core.objects.Substance_StructuralAlerts;
import org.qsari.effectopedia.core.objects.Test;
import org.qsari.effectopedia.gui.nav.UINavigator;

public class UIResources {

    private static final UIResources INSTANCE = new UIResources();

    private UIResources() {
    }

    public UIResources getGOUtils() {
        return INSTANCE;
    }

    public static UINavigator getDefaultNavigator() {
        return defaultNavigator;
    }

    public static void setDefaultNavigator(UINavigator defaultNavigator) {
        UIResources.defaultNavigator = defaultNavigator;
    }

    private static UINavigator defaultNavigator;

    public static final ImageIcon imageIconAdd = new ImageIcon(UIResources.class.getResource("res/Add15x15.png"));

    public static final ImageIcon imageIconDelete = new ImageIcon(UIResources.class.getResource("res/Delete15x15.png"));

    public static final ImageIcon imageIconReset = new ImageIcon(UIResources.class.getResource("res/Reset15x15.png"));

    public static final ImageIcon imageIconDiscuss = new ImageIcon(UIResources.class.getResource("res/Discuss15x15.png"));

    public static final ImageIcon imageIconChat = new ImageIcon(UIResources.class.getResource("res/Chat15x15.png"));

    public static final ImageIcon imageIconCopy = new ImageIcon(UIResources.class.getResource("res/Copy15x15.png"));

    public static final ImageIcon imageIconPaste = new ImageIcon(UIResources.class.getResource("res/Paste15x15.png"));

    public static final ImageIcon imageFileOpen = new ImageIcon(UIResources.class.getResource("res/FileOpenW24x36.png"));

    public static final ImageIcon imageFileSave = new ImageIcon(UIResources.class.getResource("res/FileSaveW24x36.png"));

    public static final ImageIcon imagePublish = new ImageIcon(UIResources.class.getResource("res/PublishW24x36.png"));

    public static final ImageIcon imageChemical = new ImageIcon(UIResources.class.getResource("res/Chemical24x24.png"));

    public static final ImageIcon imageReactiveChemical = new ImageIcon(UIResources.class.getResource("res/ReactiveChemical24x24.png"));

    public static final ImageIcon imageStructuralAlert = new ImageIcon(UIResources.class.getResource("res/StructuralAlert24x24.png"));

    public static final ImageIcon imageLink = new ImageIcon(UIResources.class.getResource("res/Link24x24.png"));

    public static final ImageIcon imageEffect = new ImageIcon(UIResources.class.getResource("res/Effect24x24.png"));

    public static final ImageIcon imageMIE = new ImageIcon(UIResources.class.getResource("res/MIE24x24.png"));

    public static final ImageIcon imageEndpoint = new ImageIcon(UIResources.class.getResource("res/Endpoint24x24.png"));

    public static final ImageIcon imageAdverseOutcome = new ImageIcon(UIResources.class.getResource("res/AdverseOutcome24x24.png"));

    public static final ImageIcon imageTest = new ImageIcon(UIResources.class.getResource("res/Test24x24.png"));

    public static final ImageIcon imageCLET = new ImageIcon(UIResources.class.getResource("res/CLET24x24.png"));

    public static final ImageIcon imagePathwayWizard = new ImageIcon(UIResources.class.getResource("res/PathwayW24x36.png"));

    public static final ImageIcon imageChemicalWizard = new ImageIcon(UIResources.class.getResource("res/ChemicalW24x36.png"));

    public static final ImageIcon imageUpstreamEffectWizard = new ImageIcon(UIResources.class.getResource("res/UEffectW24x36.png"));

    public static final ImageIcon imageDownstreamEffectWizard = new ImageIcon(UIResources.class.getResource("res/DEffectW24x36.png"));

    public static final ImageIcon imageZoomIn = new ImageIcon(UIResources.class.getResource("res/zoom_in_24x36.png"));

    public static final ImageIcon imageZoomOut = new ImageIcon(UIResources.class.getResource("res/zoom_out_24x36.png"));

    public static final ImageIcon imageDrag = new ImageIcon(UIResources.class.getResource("res/hand_24x36.png"));

    public static final ImageIcon imageEdit = new ImageIcon(UIResources.class.getResource("res/edit_24x36.png"));

    public static final ImageIcon imageHACenter = new ImageIcon(UIResources.class.getResource("res/HACenter24x24.png"));

    public static final ImageIcon imageHALeft = new ImageIcon(UIResources.class.getResource("res/HALeft24x24.png"));

    public static final ImageIcon imageHARight = new ImageIcon(UIResources.class.getResource("res/HARight24x24.png"));

    public static final ImageIcon imageHAFill = new ImageIcon(UIResources.class.getResource("res/HAFill24x24.png"));

    public static final ImageIcon imageVAMiddle = new ImageIcon(UIResources.class.getResource("res/VAMiddle24x24.png"));

    public static final ImageIcon imageVATop = new ImageIcon(UIResources.class.getResource("res/VATop24x24.png"));

    public static final ImageIcon imageVABottom = new ImageIcon(UIResources.class.getResource("res/VABottom24x24.png"));

    public static final ImageIcon imageVAFill = new ImageIcon(UIResources.class.getResource("res/VAFill24x24.png"));

    public static final ImageIcon imagePathway = new ImageIcon(UIResources.class.getResource("res/Pathway24x24.png"));

    public static final ImageIcon imageProperty = new ImageIcon(UIResources.class.getResource("res/Property24x24.png"));

    public static final ImageIcon imageReference = new ImageIcon(UIResources.class.getResource("res/Reference24x24.png"));

    public static final ImageIcon imageReferences = new ImageIcon(UIResources.class.getResource("res/References24x24.png"));

    public static final ImageIcon imageOverrideLWR = new ImageIcon(UIResources.class.getResource("res/OverrideRightWithLeft24x36.png"));

    public static final ImageIcon imageOverrideRWL = new ImageIcon(UIResources.class.getResource("res/OverrideLeftWithRight24x36.png"));

    public static final ImageIcon imageOverrideSelLWR = new ImageIcon(UIResources.class.getResource("res/OverrideSelRightWithLeft24x36.png"));

    public static final ImageIcon imageOverrideSelRWL = new ImageIcon(UIResources.class.getResource("res/OverrideSelLeftWithRight24x36.png"));

    public static ImageIcon getIconForClass(Class<?> objectClass) {
        if (objectClass == null) return null;
        if (objectClass.isAssignableFrom(Substance_Chemical.class)) return imageChemical; else if (objectClass.isAssignableFrom(Substance_ReactiveChemical.class)) return imageReactiveChemical; else if (objectClass.isAssignableFrom(Substance_StructuralAlerts.class)) return imageStructuralAlert; else if (objectClass.isAssignableFrom(Effect_MolecularInitiatingEvent.class)) return imageMIE; else if (objectClass.isAssignableFrom(Effect_DownstreamEffect.class)) return imageEffect; else if (objectClass.isAssignableFrom(Effect_Endpoint.class)) return imageEndpoint; else if (objectClass.isAssignableFrom(Effect_AdverseOutcome.class)) return imageAdverseOutcome; else if (Link.class.isAssignableFrom(objectClass)) return imageLink; else if (objectClass.isAssignableFrom(Test.class)) return imageTest; else if (objectClass.isAssignableFrom(Pathway.class)) return imagePathway; else if (objectClass.isAssignableFrom(ReferenceID.class)) return imageReference; else if (objectClass.isAssignableFrom(ReferenceIDs.class)) return imageReferences; else if (objectClass.isAssignableFrom(EffectopediaObject.class)) return imageProperty;
        return null;
    }

    public static Cursor openHandCursor;

    public static Cursor grabHandCursor;

    public static Cursor openHandOverObjectCursor;

    public static Cursor grabObjectWithHandCursor;

    public static Cursor chemicalCursor;

    public static Cursor effectCursor;

    public static Cursor linkCursor;

    public static Cursor testCursor;

    public static Cursor incompatibleCursor;

    public static Cursor zoomInCursor;

    public static Cursor zoomOutCursor;

    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotSpot = new Point(4, 4);
        Image image = toolkit.getImage(UIResources.class.getResource("res/hand_open32x32.png"));
        openHandCursor = toolkit.createCustomCursor(image, hotSpot, "OPEN_HAND_CURSOR");
        image = toolkit.getImage(UIResources.class.getResource("res/hand_grip32x32.png"));
        grabHandCursor = toolkit.createCustomCursor(image, hotSpot, "GRAB_HAND_CURSOR");
        image = toolkit.getImage(UIResources.class.getResource("res/hand_open_obj32x32.png"));
        openHandOverObjectCursor = toolkit.createCustomCursor(image, hotSpot, "OPEN_HAND_OVER_OBJECT_CURSOR");
        image = toolkit.getImage(UIResources.class.getResource("res/hand_grip_obj32x32.png"));
        grabObjectWithHandCursor = toolkit.createCustomCursor(image, hotSpot, "GRAB_OBJECT_HAND_CURSOR");
        image = toolkit.getImage(UIResources.class.getResource("res/chemical32x32.png"));
        chemicalCursor = toolkit.createCustomCursor(image, hotSpot, "CHEMICAL");
        image = toolkit.getImage(UIResources.class.getResource("res/effect32x32.png"));
        effectCursor = toolkit.createCustomCursor(image, hotSpot, "EFFECT");
        image = toolkit.getImage(UIResources.class.getResource("res/link32x32.png"));
        linkCursor = toolkit.createCustomCursor(image, hotSpot, "LINK");
        image = toolkit.getImage(UIResources.class.getResource("res/test32x32.png"));
        testCursor = toolkit.createCustomCursor(image, hotSpot, "TEST");
        image = toolkit.getImage(UIResources.class.getResource("res/incompatible32x32.png"));
        incompatibleCursor = toolkit.createCustomCursor(image, hotSpot, "INCOMPATIBLE");
        image = toolkit.getImage(UIResources.class.getResource("res/zoom_in32x32.png"));
        zoomInCursor = toolkit.createCustomCursor(image, hotSpot, "ZOOM_IN");
        image = toolkit.getImage(UIResources.class.getResource("res/zoom_out32x32.png"));
        zoomOutCursor = toolkit.createCustomCursor(image, hotSpot, "ZOOM_OUT");
    }
}
