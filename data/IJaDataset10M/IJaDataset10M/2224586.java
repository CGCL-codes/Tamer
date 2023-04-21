package uk.co.weft.dealersys2;

import uk.co.weft.dbutil.Context;
import uk.co.weft.htform.Auxiliary;
import uk.co.weft.htform.InitialisationException;
import uk.co.weft.htform.IntegerWidget;
import uk.co.weft.htform.TableWrapperForm;
import uk.co.weft.htform.Widget;

public class Pack extends TableWrapperForm {

    /**
	 * set up widgets to edit each of my fields
	 */
    public void init(Context config) throws InitialisationException {
        table = "PACK";
        keyField = "Pack";
        Widget w = addWidget(new Widget("Name", "Name", "A descriptive name for this pack"));
        w.setMandatory(true);
        w = addWidget(new IntegerWidget("Weight", "Weight", "The maximum weight for the content of " + "this pack in grammes."));
        w = addWidget(new IntegerWidget("Height", "Height", "The maximum height for the content of " + "this pack in millimetres."));
        w = addWidget(new IntegerWidget("Width", "Width", "The maximum width for the content of " + "this pack in millimetres."));
        w = addWidget(new IntegerWidget("Depth", "Depth", "The maximum depth for the content of " + "this pack in millimetres."));
        w = addWidget(new IntegerWidget("Weight", "Weight", "The maximum weight for the content of " + "this pack in grammes."));
        addAuxiliary(new Auxiliary("ZRATE", "ZRate", "PACK", "Pack", "zrate", "Rates for shipping this pack", "SZone"));
        super.init(config);
    }
}
