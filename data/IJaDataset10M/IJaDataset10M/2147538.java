package kr.ac.ssu.imc.whitehole.report.designer.items.rdmaskings;

import javax.swing.*;
import java.math.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class RDPercentMasking extends RDMaskingSet {

    private String[] tmp;

    private String[] temp;

    private String[] str;

    protected boolean maskChecked = false;

    protected boolean resultStateModified = false;

    protected boolean nagativeMaskChecked;

    protected boolean flag;

    public RDPercentMasking(String maskName, int selectedColumn) {
        super(maskName, selectedColumn);
    }

    public RDPercentMasking() {
        super();
    }

    public boolean isMaskChecked() {
        for (int i = 0; i < resultState.length; i++) {
            if (resultState[i] != 0) maskChecked = true;
        }
        return maskChecked;
    }

    public boolean getResultStateModified() {
        return resultStateModified;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public void setFlag(boolean input) {
        this.flag = input;
    }

    public boolean isNagativeMaskChecked() {
        for (int j = 2; j < resultState.length; j++) for (int i = 2; i < resultState.length; i++) {
            if (resultState[i] == 1) nagativeMaskChecked = true;
        }
        return nagativeMaskChecked;
    }

    public String[] masking(String[] columnDataSet) {
        tmp = new String[columnDataSet.length];
        temp = new String[columnDataSet.length];
        str = new String[columnDataSet.length];
        String[] resultColumnDataSet = new String[columnDataSet.length];
        if (resultState[0] > -1) {
            for (int i = 0; i < columnDataSet.length; i++) {
                if (columnDataSet[i].equals("") || columnDataSet[i] == null) {
                    resultColumnDataSet[i] = "";
                    continue;
                }
                tmp[i] = "";
                boolean over = false;
                String tail = "";
                if (columnDataSet[i] == null) {
                    str[i] = "";
                } else {
                    if (flag == false) {
                        if (columnDataSet[i].indexOf("%") != -1) columnDataSet[i] = columnDataSet[i].substring(0, columnDataSet[i].indexOf("%"));
                        if (columnDataSet[i].indexOf(".") == -1) {
                            try {
                                str[i] = new BigInteger(columnDataSet[i]).multiply(new BigInteger("100")).toString();
                                if (str[i].indexOf("E") != -1) {
                                    tail = Integer.toString(columnDataSet[i].substring(0, columnDataSet[i].indexOf(".")).length() + 1);
                                    str[i] = str[i].substring(0, str[i].indexOf("E"));
                                    over = true;
                                }
                            } catch (Exception ex) {
                                str[i] = columnDataSet[i];
                            }
                        } else {
                            try {
                                temp[i] = new BigDecimal(columnDataSet[i]).multiply(new BigDecimal("100.0")).toString();
                                str[i] = temp[i].substring(0, temp[i].indexOf(".")) + temp[i].substring(temp[i].indexOf("."), temp[i].length());
                                if (str[i].indexOf("E") != -1) {
                                    tail = Integer.toString(columnDataSet[i].substring(0, columnDataSet[i].indexOf(".")).length() + 1);
                                    str[i] = str[i].substring(0, str[i].indexOf("E"));
                                    over = true;
                                }
                            } catch (Exception ex) {
                                str[i] = columnDataSet[i];
                            }
                        }
                    } else {
                        if (columnDataSet[i].indexOf("%") != -1) columnDataSet[i] = columnDataSet[i].substring(0, columnDataSet[i].indexOf("%"));
                        str[i] = columnDataSet[i];
                        if (str[i].indexOf("E") != -1) {
                            tail = columnDataSet[i].substring(columnDataSet[i].indexOf("E") + 1, columnDataSet[i].length());
                            str[i] = str[i].substring(0, str[i].indexOf("E"));
                            over = true;
                        }
                    }
                }
                if (str[i].indexOf(".") != -1) {
                    int decimalPoint = str[i].indexOf(".");
                    if ((str[i].length() - decimalPoint - 1) < resultState[0]) {
                        for (int j = 0; j <= resultState[0] - (str[i].length() - decimalPoint - 1); j++) str[i] += "0";
                        resultColumnDataSet[i] = str[i];
                    } else {
                        if (resultState[0] == 0) {
                            resultColumnDataSet[i] = new String(str[i].substring(0, decimalPoint));
                        } else resultColumnDataSet[i] = new String(str[i].substring(0, decimalPoint + resultState[0] + 1));
                    }
                } else {
                    if (str[i] == "") str[i] += "0";
                    str[i] += ".";
                    for (int j = 0; j < resultState[0]; j++) str[i] += "0";
                    if (resultState[0] == 0) resultColumnDataSet[i] = str[i].substring(0, str[i].length() - 1); else resultColumnDataSet[i] = str[i];
                }
                if (over == true) resultColumnDataSet[i] = resultColumnDataSet[i] + "E" + tail;
                resultColumnDataSet[i] = resultColumnDataSet[i] + "%";
                columnDataSet[i] = columnDataSet[i] + "%";
            }
        } else {
            resultColumnDataSet = columnDataSet;
        }
        return resultColumnDataSet;
    }

    /** ���� ��ü�� ������ ��� XML ��Ҹ� �����Ѵ�.
   *  @param oDocument XML ��ť��Ʈ ��ü.
   *  @return XML ������Ʈ ��ü.
   */
    public org.w3c.dom.Element createElementNode(org.w3c.dom.Document tDocument) {
        org.w3c.dom.Element tElement = tDocument.createElement("rdqPercentMask");
        for (int i = 0; i < ((int[]) getResultState()).length; i++) {
            Element tRdqDialogState = tDocument.createElement("rdqDialogState");
            tRdqDialogState.appendChild(tDocument.createTextNode(Integer.toString(((int[]) getResultState())[i])));
            tElement.appendChild(tRdqDialogState);
        }
        return tElement;
    }

    /**
   * oElement ��带 ���� RDPercentMasking�� ���Ѵ�.
   */
    public static RDPercentMasking createRDPercentMasking(org.w3c.dom.Element oElement, int sColumn) {
        int[] rdqDialogState = new int[7];
        int dSize = 0;
        org.w3c.dom.NodeList oPercentMaskingList = oElement.getChildNodes();
        for (int i = 0; i < oPercentMaskingList.getLength(); i++) {
            org.w3c.dom.Node oPercentMaskingNode = oPercentMaskingList.item(i);
            org.w3c.dom.NodeList oPercentMaskingNodeList = oPercentMaskingNode.getChildNodes();
            if (oPercentMaskingNode.getNodeName().equals("rdqDialogState")) {
                for (int l = 0; l < oPercentMaskingNodeList.getLength(); l++) {
                    if (oPercentMaskingNodeList.item(l).getNodeType() == org.w3c.dom.Node.TEXT_NODE) rdqDialogState[dSize] = Integer.parseInt(((org.w3c.dom.Text) oPercentMaskingNodeList.item(l)).getData());
                }
                dSize++;
            }
        }
        RDPercentMasking oNewPercentMasking = new RDPercentMasking("�����", sColumn);
        oNewPercentMasking.setResultState(rdqDialogState);
        return oNewPercentMasking;
    }

    public String masking(String in) {
        String tmp, str, result;
        String temp;
        if (resultState[0] > -1) {
            if (in.equals("") || in == null) {
                result = "";
                return in;
            }
            tmp = "";
            boolean over = false;
            String tail = "";
            if (in == null) {
                str = "";
            } else {
                if (flag == false) {
                    if (in.indexOf("%") != -1) in = in.substring(0, in.indexOf("%"));
                    if (in.indexOf(".") == -1) {
                        try {
                            str = new BigInteger(in).multiply(new BigInteger("100")).toString();
                            if (str.indexOf("E") != -1) {
                                tail = Integer.toString(in.substring(0, in.indexOf(".")).length() + 1);
                                str = str.substring(0, str.indexOf("E"));
                                over = true;
                            }
                        } catch (Exception ex) {
                            str = in;
                        }
                    } else {
                        try {
                            temp = new BigDecimal(in).multiply(new BigDecimal("100.0")).toString();
                            str = temp.substring(0, temp.indexOf(".")) + temp.substring(temp.indexOf("."), temp.length());
                            if (str.indexOf("E") != -1) {
                                tail = Integer.toString(in.substring(0, in.indexOf(".")).length() + 1);
                                str = str.substring(0, str.indexOf("E"));
                                over = true;
                            }
                        } catch (Exception ex) {
                            str = in;
                        }
                    }
                } else {
                    if (in.indexOf("%") != -1) in = in.substring(0, in.indexOf("%"));
                    str = in;
                    if (str.indexOf("E") != -1) {
                        tail = in.substring(in.indexOf("E") + 1, in.length());
                        str = str.substring(0, str.indexOf("E"));
                        over = true;
                    }
                }
            }
            if (str.indexOf(".") != -1) {
                int decimalPoint = str.indexOf(".");
                if ((str.length() - decimalPoint - 1) < resultState[0]) {
                    for (int j = 0; j <= resultState[0] - (str.length() - decimalPoint - 1); j++) str += "0";
                    result = str;
                } else {
                    if (resultState[0] == 0) {
                        result = new String(str.substring(0, decimalPoint));
                    } else result = new String(str.substring(0, decimalPoint + resultState[0] + 1));
                }
            } else {
                if (str == "") str += "0";
                str += ".";
                for (int j = 0; j < resultState[0]; j++) str += "0";
                if (resultState[0] == 0) result = str.substring(0, str.length() - 1); else result = str;
            }
            if (over == true) result = result + "E" + tail;
            result = result + "%";
            in = in + "%";
        } else {
            result = in;
        }
        return result;
    }
}
