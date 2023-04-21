package xmlrpc;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.persistence.NoResultException;
import data.ProjetData;
import data.SlotgrpData;
import data.TheorieData;

/**
 * Class MatiereManager
 * For XMLRPC remote accès
 * Récupère les données sur les Matieres
 * 
 * @author frayar
 * @version 1.0
 * 
 */
public class MatiereManager {

    private InitialContext initialContext;

    private session.Course courseManager;

    public String getNom(String matiereID) {
        initContext();
        String resu = "No_Name";
        TheorieData td = courseManager.findTheorieBySigle(matiereID);
        if (td != null) {
            resu = td.getNom();
        } else {
            ProjetData pd = courseManager.findProjetBySigle(matiereID);
            if (pd != null) {
                resu = pd.getNom();
            }
        }
        return resu;
    }

    public String getCredits(String matiereID) {
        initContext();
        String resu = "No_ECTS_Credits";
        TheorieData td = courseManager.findTheorieBySigle(matiereID);
        if (td != null) {
            resu = "" + td.getCreditsECTS();
        } else {
            ProjetData pd = courseManager.findProjetBySigle(matiereID);
            if (pd != null) {
                resu = "" + pd.getCoefficient();
            }
        }
        return resu;
    }

    public String getC(String matiereID) {
        initContext();
        String resu = "No_C";
        int temp = 0;
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "cours");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        temp = temp + sd.getVolume();
                    }
                }
                resu = "" + temp + "s";
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "cours");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                temp = temp + sd.getVolume();
                            }
                        }
                        resu = "" + temp + "s";
                    }
                }
            } catch (Exception e1) {
            }
        }
        if (temp == 0) {
            resu = "No_C";
        }
        return resu;
    }

    public String getCTD(String matiereID) {
        initContext();
        String resu = "No_CTD";
        int temp = 0;
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "ctd");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        temp = temp + sd.getVolume();
                    }
                }
                resu = "" + temp + "s";
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "ctd");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                temp = temp + sd.getVolume();
                            }
                        }
                        resu = "" + temp + "s";
                    }
                }
            } catch (Exception e1) {
                resu = "No_CTD";
            }
        }
        if (temp == 0) {
            resu = "No_CTD";
        }
        return resu;
    }

    public String getTD(String matiereID) {
        initContext();
        String resu = "No_TD";
        int temp = 0;
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "td");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        temp = temp + sd.getVolume();
                    }
                }
                resu = "" + temp + "s";
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "td");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                temp = temp + sd.getVolume();
                            }
                        }
                        resu = "" + temp + "s";
                    }
                }
            } catch (Exception e1) {
                resu = "No_TD";
            }
        }
        if (temp == 0) {
            resu = "No_TD";
        }
        return resu;
    }

    public String getTP(String matiereID) {
        initContext();
        String resu = "No_TP";
        int temp = 0;
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "tp");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        temp = temp + sd.getVolume();
                    }
                }
                resu = "" + temp + "s";
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "tp");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                temp = temp + sd.getVolume();
                            }
                        }
                        resu = "" + temp + "s";
                    }
                }
            } catch (Exception e1) {
                resu = "No_TP";
            }
        }
        if (temp == 0) {
            resu = "No_TP";
        }
        return resu;
    }

    public List<String> getExamens(String matiereID) {
        initContext();
        Vector<String> resu = new Vector<String>();
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "exam");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        if (sd.getTag() != null) {
                            resu.add(sd.getTag());
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "cours");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                if (sd.getTag() != null) {
                                    resu.add(sd.getTag());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                resu.add("No_Exam");
            }
        }
        return resu;
    }

    public String getExamenCoeff(String matiereID, String examenID) {
        initContext();
        String resu = "No_Exam_Coeff";
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "exam");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        if (sd.getTag() != null) {
                            if (sd.getTag().toLowerCase().compareTo(examenID.toLowerCase()) == 0) {
                                resu = "" + sd.getCoeffExam();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "exam");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                if (sd.getTag() != null) {
                                    if (sd.getTag().toLowerCase().compareTo(examenID.toLowerCase()) == 0) {
                                        resu = "" + sd.getCoeffExam();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                resu = "No_Exam_Coeff";
            }
        }
        return resu;
    }

    public String getExamenDate(String matiereID, String examenID) {
        initContext();
        String resu = "No_Exam_Date";
        try {
            TheorieData td = courseManager.findTheorieBySigle(matiereID);
            if (td.getId() != -1) {
                List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(td.getId(), "exam");
                if (sdL != null) {
                    Iterator<SlotgrpData> it = sdL.iterator();
                    while (it.hasNext()) {
                        SlotgrpData sd = (SlotgrpData) it.next();
                        if (sd.getTag() != null) {
                            if (sd.getTag().toLowerCase().compareTo(examenID.toLowerCase()) == 0) {
                                resu = "" + sd.getDateExam();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                ProjetData pd = courseManager.findProjetBySigle(matiereID);
                if (pd != null) {
                    if (pd.getId() != -1) {
                        List<SlotgrpData> sdL = courseManager.findTheorieSlotgrpsByType(pd.getId(), "exam");
                        if (sdL != null) {
                            Iterator<SlotgrpData> it = sdL.iterator();
                            while (it.hasNext()) {
                                SlotgrpData sd = (SlotgrpData) it.next();
                                if (sd.getTag() != null) {
                                    if (sd.getTag().toLowerCase().compareTo(examenID.toLowerCase()) == 0) {
                                        resu = "" + sd.getDateExam();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e1) {
                resu = "No_Exam_Date";
            }
        }
        return resu;
    }

    public List<String> getProjets(String matiereID) {
        return null;
    }

    public String getProjetCoeff(String matiereID, String projetID) {
        return "";
    }

    public String getProjetDebut(String matiereID, String projetID) {
        return "";
    }

    public String getProjetFin(String matiereID, String projetID) {
        return "";
    }

    protected void initContext() {
        if (initialContext == null) {
            try {
                initialContext = new InitialContext();
                courseManager = (session.Course) initialContext.lookup("CourseManagerBean/remote");
            } catch (Exception e) {
                System.out.println("SyLAB_ERROR : MatiereManager > Contexte non récupéré!");
                e.printStackTrace();
            }
        }
    }
}
