package de.intarsys.pdf.pd;

import de.intarsys.pdf.cos.COSBasedObject;
import de.intarsys.pdf.cos.COSDictionary;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.cos.COSObject;

/**
 * The GoTo action.
 * <p>
 * When executed the action focuses a viewer to a new destination.
 * 
 */
public class PDActionGoTo extends PDAction {

    /**
	 * The meta class implementation
	 */
    public static class MetaClass extends PDAction.MetaClass {

        protected MetaClass(Class instanceClass) {
            super(instanceClass);
        }

        protected COSBasedObject.MetaClass doDetermineClass(COSObject object) {
            return PDActionGoTo.META;
        }
    }

    /** The meta class instance */
    public static final MetaClass META = new MetaClass(MetaClass.class.getDeclaringClass());

    public static final COSName CN_ActionType_GoTo = COSName.constant("GoTo");

    public static final COSName DK_D = COSName.constant("D");

    private PDDestination destination;

    protected PDActionGoTo(COSObject object) {
        super(object);
    }

    public COSName cosGetExpectedActionType() {
        return CN_ActionType_GoTo;
    }

    protected void initializeFromCos() {
        super.initializeFromCos();
    }

    public PDDestination getDestination() {
        if (destination == null) {
            COSObject destObject;
            if (cosGetObject() instanceof COSDictionary) {
                destObject = cosGetField(DK_D);
            } else {
                destObject = cosGetObject();
            }
            destination = (PDDestination) PDDestination.META.createFromCos(destObject);
        }
        return destination;
    }

    public void invalidateCaches() {
        destination = null;
        super.invalidateCaches();
    }
}
