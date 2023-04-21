package servlet.queries;

/**
 *
 * @author  Administrator
 */
public class DupCheckHandler {

    private ejb.bprocess.util.NewGenXMLGenerator newGenXMLGenerator = null;

    private ejb.bprocess.util.Utility utility = null;

    private ejb.bprocess.util.HomeFactory homeFactory = null;

    /** Creates a new instance of DupCheckHandler */
    public DupCheckHandler(ejb.bprocess.queries.DupCheckSessionHome home) {
        this.home = home;
        this.utility = ejb.bprocess.util.Utility.getInstance(null);
    }

    public String getRequestProcess(org.jdom.Document doc) {
        String xml = "";
        String titlewords = doc.getRootElement().getChildText("TitleWords");
        System.out.println("Titlewords==========" + titlewords);
        String libid = doc.getRootElement().getChildText("LibraryId");
        org.jdom.Element root = new org.jdom.Element("Response");
        try {
            ejb.bprocess.queries.DupCheckSession dc = home.create();
            java.util.Vector vec = new java.util.Vector();
            vec = dc.getRequestProcessDetails(titlewords, libid);
            System.out.println("vec size is" + vec.size());
            java.util.Vector v1 = (java.util.Vector) vec.elementAt(0);
            java.util.Vector v2 = (java.util.Vector) vec.elementAt(1);
            System.out.println("v2 size is+++++++++++++++" + v2.size());
            java.util.Vector v3 = (java.util.Vector) vec.elementAt(2);
            System.out.println("v3 size is++++++++++++++" + v3.size());
            java.util.Vector v4 = (java.util.Vector) vec.elementAt(3);
            System.out.println("v4 size is++++++++++++" + v4.size());
            org.jdom.Element topicaldetails = new org.jdom.Element("TitleDetails");
            if (v1.size() > 0) {
                System.out.println("v1.size is+++++++++++++++++" + v1.size());
                for (int i = 0; i < v1.size(); i = i + 8) {
                    org.jdom.Element topical = new org.jdom.Element("TitleName");
                    org.jdom.Element ele1 = new org.jdom.Element("Title");
                    ele1.setText(utility.getTestedString(v1.elementAt(i)));
                    topical.addContent(ele1);
                    org.jdom.Element ele2 = new org.jdom.Element("Author");
                    ele2.setText(utility.getTestedString(v1.elementAt(i + 1)));
                    topical.addContent(ele2);
                    org.jdom.Element ele3 = new org.jdom.Element("Publisher");
                    ele3.setText(utility.getTestedString(v1.elementAt(i + 2)));
                    topical.addContent(ele3);
                    String s1 = utility.getTestedString(v1.elementAt(i + 3));
                    String s2 = utility.getTestedString(v1.elementAt(i + 4));
                    if (s1.equals("A")) {
                        org.jdom.Element ele4 = new org.jdom.Element("Status");
                        ele4.setText(s2 + "(Request recived)");
                        topical.addContent(ele4);
                    } else if (s1.equals("B")) {
                        org.jdom.Element ele4 = new org.jdom.Element("Status");
                        ele4.setText(s2 + "(Awaiting approval)");
                        topical.addContent(ele4);
                    } else if (s1.equals("C")) {
                        String s3 = utility.getTestedString(v1.elementAt(i + 5));
                        String s4 = utility.getTestedString(v1.elementAt(i + 6));
                        String s5 = utility.getTestedString(v1.elementAt(i + 7));
                        if (s3.equals("A")) {
                            if (!s4.equals("")) {
                                org.jdom.Element ele4 = new org.jdom.Element("Status");
                                ele4.setText(s4 + "(pending order)");
                                topical.addContent(ele4);
                            } else {
                                org.jdom.Element ele4 = new org.jdom.Element("Status");
                                ele4.setText("");
                                topical.addContent(ele4);
                            }
                        } else if (s3.equals("B")) {
                            if (!s4.equals("")) {
                                org.jdom.Element ele4 = new org.jdom.Element("Status");
                                ele4.setText(s4 + "(Addded to order list)");
                                topical.addContent(ele4);
                            } else {
                                org.jdom.Element ele4 = new org.jdom.Element("Status");
                                ele4.setText("");
                                topical.addContent(ele4);
                            }
                        } else if (s3.equals("C")) {
                            if (!s4.equals("")) {
                                org.jdom.Element ele5 = new org.jdom.Element("Status");
                                ele5.setText(s4 + "(Item to be accessioned)");
                                topical.addContent(ele5);
                            } else {
                                org.jdom.Element ele5 = new org.jdom.Element("Status");
                                ele5.setText("");
                                topical.addContent(ele5);
                            }
                            if (!s5.equals("")) {
                                org.jdom.Element ele6 = new org.jdom.Element("Status1");
                                ele6.setText(s5 + "(Awaiting technical processiong)");
                                topical.addContent(ele6);
                            } else {
                                org.jdom.Element ele6 = new org.jdom.Element("Status1");
                                ele6.setText("");
                                topical.addContent(ele6);
                            }
                        } else if (s3.equals("D")) {
                            if (!s4.equals("")) {
                                org.jdom.Element ele5 = new org.jdom.Element("Status");
                                ele5.setText(s4 + "(Item to be accessioned)");
                                topical.addContent(ele5);
                            } else {
                                org.jdom.Element ele5 = new org.jdom.Element("Status");
                                ele5.setText("");
                                topical.addContent(ele5);
                            }
                            if (!s5.equals("")) {
                                org.jdom.Element ele6 = new org.jdom.Element("Status1");
                                ele6.setText(s5 + "(Awaiting technical processiong)");
                                topical.addContent(ele6);
                            } else {
                                org.jdom.Element ele6 = new org.jdom.Element("Status1");
                                ele6.setText("");
                                topical.addContent(ele6);
                            }
                        }
                    }
                    topicaldetails.addContent(topical);
                }
            }
            root.addContent(topicaldetails);
            org.jdom.Element approvaldetails = new org.jdom.Element("ApprovalDetails");
            if (v2.size() > 0) {
                System.out.println("v2 size is++++++++++++++++++++++" + v2.size());
                for (int j = 0; j < v2.size(); j = j + 8) {
                    String title = v2.elementAt(j).toString();
                    String author = v2.elementAt(j + 1).toString();
                    String publisher = v2.elementAt(j + 2).toString();
                    String status = v2.elementAt(j + 3).toString();
                    String ss5 = v2.elementAt(j + 4).toString();
                    String ss6 = v2.elementAt(j + 5).toString();
                    String ss7 = v2.elementAt(j + 6).toString();
                    String ss8 = v2.elementAt(j + 7).toString();
                    org.jdom.Element approval = new org.jdom.Element("ApprovalName");
                    org.jdom.Element ele1 = new org.jdom.Element("Title2");
                    ele1.setText(utility.getTestedString(title));
                    approval.addContent(ele1);
                    org.jdom.Element ele2 = new org.jdom.Element("Author2");
                    ele2.setText(utility.getTestedString(author));
                    approval.addContent(ele2);
                    org.jdom.Element ele3 = new org.jdom.Element("Publisher2");
                    ele3.setText(utility.getTestedString(publisher));
                    approval.addContent(ele3);
                    String s1 = utility.getTestedString(status);
                    String s2 = utility.getTestedString(ss5);
                    if (s1.equals("A")) {
                        if (!s2.equals("")) {
                            org.jdom.Element ele4 = new org.jdom.Element("Status2");
                            ele4.setText(s2 + "(Item request On-Approval)");
                            approval.addContent(ele4);
                        } else {
                            org.jdom.Element ele4 = new org.jdom.Element("Status2");
                            ele4.setText("");
                            approval.addContent(ele4);
                        }
                    } else if (s1.equals("B")) {
                        if (!s2.equals("")) {
                            org.jdom.Element ele4 = new org.jdom.Element("Status2");
                            ele4.setText(s2 + "(Item received On-Approval)");
                            approval.addContent(ele4);
                        } else {
                            org.jdom.Element ele4 = new org.jdom.Element("Status2");
                            ele4.setText("");
                            approval.addContent(ele4);
                        }
                    } else if (s1.equals("C")) {
                        if (ss6.equals("A")) {
                            if (!ss7.equals("")) {
                                org.jdom.Element ele4 = new org.jdom.Element("Status2");
                                ele4.setText(ss7 + "(Ready to order)");
                                approval.addContent(ele4);
                            } else {
                                org.jdom.Element ele4 = new org.jdom.Element("Status2");
                                ele4.setText("");
                                approval.addContent(ele4);
                            }
                        } else if (ss6.equals("B")) {
                            if (!ss7.equals("")) {
                                org.jdom.Element ele4 = new org.jdom.Element("Status2");
                                ele4.setText(ss7 + "(Added to order)");
                                approval.addContent(ele4);
                            } else {
                                org.jdom.Element ele4 = new org.jdom.Element("Status2");
                                ele4.setText("");
                                approval.addContent(ele4);
                            }
                        } else if (ss6.equals("C")) {
                            if (!ss7.equals("")) {
                                org.jdom.Element ele5 = new org.jdom.Element("Status2");
                                ele5.setText(ss7 + "(Item Accessioned)");
                                approval.addContent(ele5);
                            } else {
                                org.jdom.Element ele5 = new org.jdom.Element("Status2");
                                ele5.setText("");
                                approval.addContent(ele5);
                            }
                            if (!ss8.equals("")) {
                                org.jdom.Element ele6 = new org.jdom.Element("Status3");
                                ele6.setText(ss8 + "(Item Received)");
                                approval.addContent(ele6);
                            } else {
                                org.jdom.Element ele6 = new org.jdom.Element("Status3");
                                ele6.setText("");
                                approval.addContent(ele6);
                            }
                        } else if (ss6.equals("D")) {
                            if (!ss7.equals("")) {
                                org.jdom.Element ele5 = new org.jdom.Element("Status2");
                                ele5.setText(ss7 + "(Item Accessioned)");
                                approval.addContent(ele5);
                            } else {
                                org.jdom.Element ele5 = new org.jdom.Element("Status2");
                                ele5.setText("");
                                approval.addContent(ele5);
                            }
                            if (!ss8.equals("")) {
                                org.jdom.Element ele6 = new org.jdom.Element("Status3");
                                ele6.setText(ss8 + "(Item Received)");
                                approval.addContent(ele6);
                            } else {
                                org.jdom.Element ele6 = new org.jdom.Element("Status3");
                                ele6.setText("");
                                approval.addContent(ele6);
                            }
                        }
                    }
                    approvaldetails.addContent(approval);
                }
            }
            root.addContent(approvaldetails);
            org.jdom.Element soliciteddetails = new org.jdom.Element("SolicitedDetails");
            if (v3.size() > 0) {
                for (int t = 0; t < v3.size(); t = t + 6) {
                    String title = utility.getTestedString(v3.elementAt(t));
                    String author = utility.getTestedString(v3.elementAt(t + 1));
                    String publisher = utility.getTestedString(v3.elementAt(t + 2));
                    String status = utility.getTestedString(v3.elementAt(t + 3));
                    String accno = utility.getTestedString(v3.elementAt(t + 4));
                    String ss6 = utility.getTestedString(v3.elementAt(t + 5));
                    org.jdom.Element solicited = new org.jdom.Element("SolicitedName");
                    org.jdom.Element ele1 = new org.jdom.Element("Title2");
                    ele1.setText(utility.getTestedString(title));
                    solicited.addContent(ele1);
                    org.jdom.Element ele2 = new org.jdom.Element("Author2");
                    ele2.setText(utility.getTestedString(author));
                    solicited.addContent(ele2);
                    org.jdom.Element ele3 = new org.jdom.Element("Publisher2");
                    ele3.setText(utility.getTestedString(publisher));
                    solicited.addContent(ele3);
                    if (status.equals("A")) {
                        org.jdom.Element ele4 = new org.jdom.Element("Status3");
                        ele4.setText(ss6 + "(Item solicited as gift)");
                        solicited.addContent(ele4);
                    }
                    if (status.equals("B") && (accno.equals(""))) {
                        org.jdom.Element ele5 = new org.jdom.Element("Status3");
                        ele5.setText(ss6 + "(Item received as gift)");
                        solicited.addContent(ele5);
                    }
                    if (status.equals("B") && (accno != null)) {
                        org.jdom.Element ele5 = new org.jdom.Element("Status3");
                        ele5.setText(ss6 + "(Item accessioned)");
                        solicited.addContent(ele5);
                    }
                    soliciteddetails.addContent(solicited);
                }
            }
            root.addContent(soliciteddetails);
            org.jdom.Element cataloguedetails = new org.jdom.Element("CatalogueDetails");
            if (v4.size() > 0) {
                for (int b = 0; b < v4.size(); b = b + 6) {
                    String title = v4.elementAt(b).toString();
                    String author = v4.elementAt(b + 1).toString();
                    String edition = v4.elementAt(b + 2).toString();
                    String isbn = v4.elementAt(b + 3).toString();
                    String publisher = v4.elementAt(b + 4).toString();
                    String c = v4.elementAt(b + 5).toString();
                    System.out.println("c value is" + c);
                    org.jdom.Element catalogue = new org.jdom.Element("CatalogueName");
                    org.jdom.Element ele1 = new org.jdom.Element("Title2");
                    ele1.setText(utility.getTestedString(title));
                    catalogue.addContent(ele1);
                    org.jdom.Element ele2 = new org.jdom.Element("Author2");
                    ele2.setText(utility.getTestedString(author));
                    catalogue.addContent(ele2);
                    org.jdom.Element ele3 = new org.jdom.Element("Publisher2");
                    ele3.setText(utility.getTestedString(publisher));
                    catalogue.addContent(ele3);
                    org.jdom.Element ele4 = new org.jdom.Element("Status4");
                    ele4.setText(utility.getTestedString(c + "(Available in library)"));
                    catalogue.addContent(ele4);
                    cataloguedetails.addContent(catalogue);
                }
            }
            root.addContent(cataloguedetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        org.jdom.Document doc1 = new org.jdom.Document(root);
        xml = (new org.jdom.output.XMLOutputter()).outputString(doc1);
        System.out.println("xml is++++++++" + xml);
        return xml;
    }

    private ejb.bprocess.queries.DupCheckSessionHome home;
}
