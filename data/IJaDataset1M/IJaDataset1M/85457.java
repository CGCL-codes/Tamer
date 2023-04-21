package pck_tap.Userinterface.rpe.frmRecipe.JPanels.JMaltPanel.jPanelMalt;

import java.util.Collections;
import java.util.Comparator;

/**
 * @author Bastie - Sebastian Ritter
 */
public class TableAbstractTableModelSort extends MaltTableAbstractTableModel {

    boolean asc_o0 = false;

    boolean asc_o1 = false;

    boolean asc_o2 = false;

    boolean asc_o3 = false;

    boolean asc_o4 = false;

    public void sort(final int spalte) {
        switch(spalte) {
            case 0:
                if (asc_o0 == true) {
                    asc_o0 = false;
                } else {
                    asc_o0 = true;
                }
            case 1:
                if (asc_o1 == true) {
                    asc_o1 = false;
                } else {
                    asc_o1 = true;
                }
            case 2:
                if (asc_o2 == true) {
                    asc_o2 = false;
                } else {
                    asc_o2 = true;
                }
            case 3:
                if (asc_o3 == true) {
                    asc_o3 = false;
                } else {
                    asc_o3 = true;
                }
            case 4:
                if (asc_o4 == true) {
                    asc_o4 = false;
                } else {
                    asc_o4 = true;
                }
            default:
        }
        Collections.sort(vectorData, new RowComparator(spalte));
    }

    private class RowComparator implements Comparator {

        private final int spalte;

        public RowComparator(final int spalte) {
            this.spalte = spalte;
        }

        public int compare(Object o1, Object o2) {
            String to1;
            String to2;
            Integer no1;
            Integer no2;
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o1 instanceof MaltTableRowData && o2 instanceof MaltTableRowData) {
                switch(this.spalte) {
                    case 0:
                        to1 = ((MaltTableRowData) o1).getFermentable().getName();
                        to2 = ((MaltTableRowData) o2).getFermentable().getName();
                        if (to1 == null && to2 == null) {
                            return 0;
                        } else if (to1 != null && to2 == null) {
                            return -1;
                        } else if (to1 == null && to2 != null) {
                            return 1;
                        } else {
                            if (asc_o0 == false) {
                                return to1.compareTo(to2);
                            } else {
                                return to2.compareTo(to1);
                            }
                        }
                    case 1:
                        return ComporeNumbers(((MaltTableRowData) o1).getFermentable().getTap_Percent(), ((MaltTableRowData) o2).getFermentable().getTap_Percent(), asc_o1);
                    case 2:
                        to1 = ((MaltTableRowData) o1).getFermentable().getAmount().toString();
                        try {
                            to2 = ((MaltTableRowData) o2).getFermentable().getAmount().toString();
                        } catch (Exception e) {
                            to2 = "";
                        }
                        if (to1.equalsIgnoreCase("")) {
                            to1 = null;
                        }
                        if (to2.equalsIgnoreCase("")) {
                            to2 = null;
                        }
                        if (to1 == null && to2 == null) {
                            return 0;
                        } else if (to1 != null && to2 == null) {
                            return -1;
                        } else if (to1 == null && to2 != null) {
                            return 1;
                        } else {
                            if (asc_o3 == false) {
                                Double d1 = ((MaltTableRowData) o1).getFermentable().getAmount();
                                Double d2 = ((MaltTableRowData) o2).getFermentable().getAmount();
                                return d1.compareTo(d2);
                            } else {
                                Double d1 = ((MaltTableRowData) o1).getFermentable().getAmount();
                                Double d2 = ((MaltTableRowData) o2).getFermentable().getAmount();
                                return d2.compareTo(d1);
                            }
                        }
                    case 3:
                        to1 = ((MaltTableRowData) o1).getFermentable().getTap_CalculatedColor().toString();
                        try {
                            to2 = ((MaltTableRowData) o2).getFermentable().getTap_CalculatedColor().toString();
                        } catch (Exception e) {
                            to2 = "";
                        }
                        if (to1.equalsIgnoreCase("")) {
                            to1 = null;
                        }
                        if (to2.equalsIgnoreCase("")) {
                            to2 = null;
                        }
                        if (to1 == null && to2 == null) {
                            return 0;
                        } else if (to1 != null && to2 == null) {
                            return -1;
                        } else if (to1 == null && to2 != null) {
                            return 1;
                        } else {
                            if (asc_o3 == false) {
                                Double d1 = ((MaltTableRowData) o1).getFermentable().getTap_CalculatedColor();
                                Double d2 = ((MaltTableRowData) o2).getFermentable().getTap_CalculatedColor();
                                return d1.compareTo(d2);
                            } else {
                                Double d1 = ((MaltTableRowData) o1).getFermentable().getTap_CalculatedColor();
                                Double d2 = ((MaltTableRowData) o2).getFermentable().getTap_CalculatedColor();
                                return d2.compareTo(d1);
                            }
                        }
                    default:
                        return 0;
                }
            } else {
                return 1;
            }
        }
    }

    private Integer ComporeNumbers(Double i1, Double i2, Boolean sortOrder) {
        String to1;
        String to2;
        if (i1 != null) {
            to1 = i1.toString();
        } else {
            to1 = null;
        }
        if (i2 != null) {
            to2 = i2.toString();
        } else {
            to2 = null;
        }
        if (to1 == null && to2 == null) {
            return 0;
        } else if (to1 != null && to2 == null) {
            return -1;
        } else if (to1 == null && to2 != null) {
            return 1;
        } else {
            if (sortOrder == false) {
                return i1.compareTo(i2);
            } else {
                return i2.compareTo(i1);
            }
        }
    }

    public String nvl(String a, String b) {
        return isNull(a) ? b : a;
    }

    public boolean isNull(Object obj) {
        return obj == null || obj.toString().trim().length() == 0;
    }
}
