package at.rc.tacos.client.ui.providers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.Transport;

public class JournalViewLabelProvider extends BaseLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

    public static final int COLUMN_LOCK = 0;

    public static final int COlUMN_OS = 1;

    public static final int COLUMN_TRANSPORT_NUMBER = 2;

    public static final int COLUMN_PRIORITY = 3;

    public static final int COLUMN_TRANSPORT_FROM = 4;

    public static final int COLUMN_PATIENT = 5;

    public static final int COLUMN_TRANSPORT_TO = 6;

    public static final int COLUMN_ERKR_VERL = 7;

    public static final int COLUMN_AE = 8;

    public static final int COLUMN_S1 = 9;

    public static final int COLUMN_S2 = 10;

    public static final int COLUMN_S3 = 11;

    public static final int COLUMN_S4 = 12;

    public static final int COLUMN_S5 = 13;

    public static final int COLUMN_S6 = 14;

    public static final int COLUMN_FZG = 15;

    public static final int COLUMN_DRIVER = 16;

    public static final int COLUMN_PARAMEDIC_I = 17;

    public static final int COLUMN_PARAMEDIC_II = 18;

    public static final int COLUMN_CALLER_NAME = 19;

    private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        Transport transport = (Transport) element;
        switch(columnIndex) {
            case COLUMN_LOCK:
                if (transport.isLocked()) return imageRegistry.get("resource.lock"); else return imageRegistry.get("empty.image24");
            case COLUMN_TRANSPORT_FROM:
                if (!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE)) return null;
                if (!transport.isBackTransport()) return null;
                long s5Time = transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE);
                Calendar cal5 = Calendar.getInstance();
                cal5.setTimeInMillis(s5Time);
                cal5.set(1970, 01, 01);
                Calendar cal4 = Calendar.getInstance();
                cal4.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                cal4.set(1970, 01, 01);
                cal4.set(Calendar.HOUR_OF_DAY, cal4.get(Calendar.HOUR_OF_DAY) - 4);
                if (cal5.getTimeInMillis() > cal4.getTimeInMillis()) return imageRegistry.get("transport.backtransport");
                return null;
            default:
                return null;
        }
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        Transport transport = (Transport) element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        switch(columnIndex) {
            case COLUMN_LOCK:
                return null;
            case COlUMN_OS:
                if (transport.getVehicleDetail() == null) {
                    return null;
                }
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("Kapfenberg")) return "KA";
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("Bruck an der Mur")) return "BM";
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("St. Marein")) return "MA";
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("Breitenau")) return "BR";
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("Th�rl")) return "TH";
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("Turnau")) return "TU";
                if (transport.getVehicleDetail().getCurrentStation().getLocationName().equalsIgnoreCase("Bezirk: Bruck - Kapfenberg")) return "BE";
            case COLUMN_TRANSPORT_NUMBER:
                if (transport.getTransportNumber() == Transport.TRANSPORT_CANCLED) return "STORNO";
                if (transport.getTransportNumber() == Transport.TRANSPORT_FORWARD) return "WTGL";
                if (transport.getTransportNumber() == Transport.TRANSPORT_NEF) return "NEF";
                return String.valueOf(transport.getTransportNumber());
            case COLUMN_PRIORITY:
                if (transport.getTransportPriority().equalsIgnoreCase("A")) return "1";
                if (transport.getTransportPriority().equalsIgnoreCase("B")) return "2";
                if (transport.getTransportPriority().equalsIgnoreCase("C")) return "3";
                if (transport.getTransportPriority().equalsIgnoreCase("D")) return "4";
                if (transport.getTransportPriority().equalsIgnoreCase("E")) return "5";
                if (transport.getTransportPriority().equalsIgnoreCase("F")) return "6";
                if (transport.getTransportPriority().equalsIgnoreCase("G")) return "7";
                return null;
            case COLUMN_TRANSPORT_FROM:
                return transport.getFromStreet() + " / " + transport.getFromCity();
            case COLUMN_PATIENT:
                String patient = "";
                if (transport.isAssistantPerson()) patient = "+";
                if (transport.getPatient() != null) return patient + " " + transport.getPatient().getLastname() + " " + transport.getPatient().getFirstname();
                return patient;
            case COLUMN_TRANSPORT_TO:
                String label = "";
                if (transport.getToStreet() != null) label += transport.getToStreet();
                if (transport.getToCity() != null) label += " / " + transport.getToCity();
                return label;
            case COLUMN_ERKR_VERL:
                if (transport.getKindOfIllness() != null) return transport.getKindOfIllness().getDiseaseName(); else return "";
            case COLUMN_AE:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_S1:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_S2:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_S3:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_S4:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_S5:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_S6:
                if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION));
                    return sdf.format(cal.getTime());
                }
                return null;
            case COLUMN_FZG:
                if (transport.getVehicleDetail() != null) {
                    return transport.getVehicleDetail().getVehicleName();
                }
                return "";
            case COLUMN_DRIVER:
                if (transport.getVehicleDetail() == null) {
                    return null;
                }
                StaffMember driver = transport.getVehicleDetail().getDriver();
                if (driver == null) {
                    return null;
                }
                return driver.getLastName() + " " + driver.getFirstName();
            case COLUMN_PARAMEDIC_I:
                if (transport.getVehicleDetail() == null) {
                    return null;
                }
                StaffMember paramedic = transport.getVehicleDetail().getFirstParamedic();
                if (paramedic == null) {
                    return null;
                }
                return paramedic.getLastName() + " " + paramedic.getFirstName();
            case COLUMN_PARAMEDIC_II:
                if (transport.getVehicleDetail() == null) {
                    return null;
                }
                StaffMember paramedic2 = transport.getVehicleDetail().getSecondParamedic();
                if (paramedic2 == null) {
                    return null;
                }
                return paramedic2.getLastName() + " " + paramedic2.getFirstName();
            default:
                return null;
        }
    }

    @Override
    public Color getBackground(Object element, int columnIndex) {
        Transport transport = (Transport) element;
        if (transport.getTransportPriority().equalsIgnoreCase("A")) return CustomColors.BACKGROUND_RED;
        if (transport.getTransportPriority().equalsIgnoreCase("B")) return CustomColors.BACKGROUND_BLUE;
        return null;
    }

    @Override
    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    @Override
    public Font getFont(Object element, int columnIndex) {
        return CustomColors.APPLICATION_DATA_FONT;
    }
}
