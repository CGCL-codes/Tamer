package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import at.rc.tacos.platform.iface.IKindOfTransport;
import at.rc.tacos.platform.model.SickPerson;

public class SickPersonTableLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

    public static final int COLUMN_LASTNAME = 0;

    public static final int COLUMN_FIRSTNAME = 1;

    public static final int COLUMN_STREET = 2;

    public static final int COLUMN_CITY = 3;

    public static final int COLUMN_SEX = 4;

    public static final int COLUMN_SVNR = 5;

    public static final int COLUMN_TA = 6;

    public static final int COLUMN_NOTES = 7;

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        SickPerson person = (SickPerson) element;
        switch(columnIndex) {
            case COLUMN_LASTNAME:
                return person.getLastName();
            case COLUMN_FIRSTNAME:
                return person.getFirstName();
            case COLUMN_STREET:
                return person.getStreetname();
            case COLUMN_CITY:
                return person.getCityname();
            case COLUMN_SEX:
                if (person.isMale()) return "Herr";
                return "Frau";
            case COLUMN_SVNR:
                return person.getSVNR();
            case COLUMN_TA:
                if (person.getKindOfTransport() == null) {
                    return null;
                }
                if (person.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_TRAGSESSEL)) return "S";
                if (person.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_KRANKENTRAGE)) return "L";
                if (person.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_GEHEND)) return "G";
                if (person.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_ROLLSTUHL)) return "R";
                return "";
            case COLUMN_NOTES:
                return person.getNotes();
            default:
                return null;
        }
    }
}
