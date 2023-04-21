package ajaxservlet.formator;

import java.util.*;
import saadadb.collection.*;
import saadadb.util.Messenger;

/**
 * @author laurentmichel
 * * @version $Id: NativeAttributesProvider.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class NativeAttributesProvider {

    private SaadaInstance saadai;

    private int cat;

    public NativeAttributesProvider(SaadaInstance si) {
        saadai = si;
        cat = si.getCategory();
    }

    public String getNativeAttr(String attr_name) {
        switch(cat) {
            case Category.ENTRY:
                return getEntryNative(attr_name);
            case Category.FLATFILE:
                return getFlatfileNative(attr_name);
            case Category.IMAGE:
                return getImageNative(attr_name);
            case Category.MISC:
                return getMiscNative(attr_name);
            case Category.SPECTRUM:
                return getSpectrumNative(attr_name);
            case Category.TABLE:
                return getTableNative(attr_name);
            default:
                Messenger.printMsg(Messenger.TRACE, "Couldn't find category.");
        }
        return null;
    }

    public String getPosNative(String attr_name) {
        if (attr_name.compareTo("pos_ra_csa") == 0) {
            return DefaultFormats.getString(((Position) saadai).getPos_ra_csa());
        } else if (attr_name.compareTo("sky_pixel_csa") == 0) {
            return DefaultFormats.getString(((Position) saadai).getSky_pixel_csa());
        } else if (attr_name.compareTo("error_maj_csa") == 0) {
            return DefaultFormats.getString(((Position) saadai).getError_maj_csa());
        } else if (attr_name.compareTo("error_min_csa") == 0) {
            return DefaultFormats.getString(((Position) saadai).getError_min_csa());
        } else if (attr_name.compareTo("error_angle_csa") == 0) {
            return DefaultFormats.getString(((Position) saadai).getError_angle_csa());
        }
        return null;
    }

    public String getSINative(String attr_name) {
        if (attr_name.compareTo("oidsaada") == 0) {
            return DefaultFormats.getString(saadai.getOid());
        } else if (attr_name.compareTo("oidproduct") == 0) {
            return DefaultFormats.getString(saadai.getOidproduct());
        } else if (attr_name.compareTo("contentsignature") == 0) {
            return DefaultFormats.getString(saadai.getContentsignature());
        } else if (attr_name.compareTo("namesaada") == 0) {
            return DefaultFormats.getString(saadai.getNameSaada());
        } else if (attr_name.compareTo("date_load") == 0) {
            return DefaultFormats.getString(saadai.getDateLoad());
        } else if (attr_name.compareTo("access_right") == 0) {
            return DefaultFormats.getString(saadai.getAccessRight());
        }
        return null;
    }

    public String getWCSNative(String attr_name) {
        if (attr_name.compareTo("crpix1_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCrpix1_csa());
        } else if (attr_name.compareTo("crpix2_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCrpix2_csa());
        } else if (attr_name.compareTo("ctype1_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCtype1_csa());
        } else if (attr_name.compareTo("ctype2_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCtype2_csa());
        } else if (attr_name.compareTo("cd1_1_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCd1_1_csa());
        } else if (attr_name.compareTo("cd1_2_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCd1_2_csa());
        } else if (attr_name.compareTo("cd2_1_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCd2_1_csa());
        } else if (attr_name.compareTo("cd2_2_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCd2_2_csa());
        } else if (attr_name.compareTo("crota_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCrota_csa());
        } else if (attr_name.compareTo("crval1_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCrval1_csa());
        } else if (attr_name.compareTo("crval2_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getCrval2_csa());
        } else if (attr_name.compareTo("product_url_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getProduct_url_csa());
        }
        return null;
    }

    public String getEntryNative(String attr_name) {
        String result = this.getPosNative(attr_name);
        if (result != null) return result;
        if (attr_name.compareTo("oidtable") == 0) {
            return DefaultFormats.getString(((EntrySaada) saadai).getOidtable());
        }
        return null;
    }

    public String getFlatfileNative(String attr_name) {
        String result = this.getSINative(attr_name);
        if (result != null) return result;
        if (attr_name.compareTo("product_url_csa") == 0) {
            return DefaultFormats.getString((saadai).getProduct_url_csa());
        }
        return null;
    }

    public String getImageNative(String attr_name) {
        String result = this.getWCSNative(attr_name);
        if (result != null) return result;
        if (attr_name.compareTo("size_alpha_csa") == 0) {
            return DefaultFormats.getString(((ImageSaada) saadai).getSize_alpha_csa());
        } else if (attr_name.compareTo("size_delta_csa") == 0) {
            return DefaultFormats.getString(((ImageSaada) saadai).getSize_delta_csa());
        } else if (attr_name.compareTo("shape_csa") == 0) {
            return DefaultFormats.getString(((ImageSaada) saadai).getShape_csa());
        } else if (attr_name.compareTo("naxis1") == 0) {
            return DefaultFormats.getString(((ImageSaada) saadai).getNaxis1());
        }
        return null;
    }

    public String getMiscNative(String attr_name) {
        String result = this.getSINative(attr_name);
        if (result != null) return result;
        if (attr_name.compareTo("product_url_csa") == 0) {
            return DefaultFormats.getString(((WCSSaada) saadai).getProduct_url_csa());
        }
        return null;
    }

    public String getSpectrumNative(String attr_name) {
        String result = this.getWCSNative(attr_name);
        if (result != null) return result;
        if (attr_name.compareTo("x_min_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_min_csa());
        } else if (attr_name.compareTo("x_max_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_max_csa());
        } else if (attr_name.compareTo("x_type_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_type_csa());
        } else if (attr_name.compareTo("x_unit_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_unit_csa());
        } else if (attr_name.compareTo("x_naxis_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_naxis_csa());
        } else if (attr_name.compareTo("x_colname_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_colname_csa());
        } else if (attr_name.compareTo("x_min_org_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_min_org_csa());
        } else if (attr_name.compareTo("x_max_org_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_max_org_csa());
        } else if (attr_name.compareTo("x_unit_org_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getX_unit_org_csa());
        } else if (attr_name.compareTo("y_min_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getY_max_csa());
        } else if (attr_name.compareTo("y_max_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getY_min_csa());
        } else if (attr_name.compareTo("y_unit_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getY_unit_csa());
        } else if (attr_name.compareTo("y_colname_csa") == 0) {
            return DefaultFormats.getString(((SpectrumSaada) saadai).getY_colname_csa());
        }
        return null;
    }

    public String getTableNative(String attr_name) {
        String result = this.getSINative(attr_name);
        if (result != null) return result;
        if (attr_name.compareTo("product_url_csa") == 0) {
            return DefaultFormats.getString(((TableSaada) saadai).getProduct_url_csa());
        } else if (attr_name.compareTo("nb_rows_csa") == 0) {
            return DefaultFormats.getString(((TableSaada) saadai).getNumberRows());
        }
        return null;
    }
}
