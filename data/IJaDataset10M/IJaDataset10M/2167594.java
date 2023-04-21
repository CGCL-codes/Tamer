package net.matmas.pneditor.actions;

import java.awt.event.ActionEvent;
import java.util.Set;
import javax.xml.soap.Node;
import net.matmas.pnapi.Arc;
import net.matmas.pnapi.PetriNet;
import net.matmas.pnapi.Place;
import net.matmas.pnapi.Transition;
import net.matmas.pneditor.PNEditor;
import net.matmas.pneditor.properties.OutPutTimeDialog;
import net.matmas.util.GraphicsTools;

public class AnalyzeAction extends Action {

    public Place start;

    public Node dalsi;

    public Place end;

    public Place dalsiP;

    public Place startO;

    public double lam;

    public Transition startA;

    public Transition dalsiT;

    public Set<Arc> von;

    public double caseprob = 1;

    public double casetime;

    public double casewaittime;

    public AnalyzeAction() {
        String name = "Time analyze";
        putValue(NAME, name);
        putValue(SMALL_ICON, GraphicsTools.getIcon("pneditor/timeanal.png"));
        putValue(SHORT_DESCRIPTION, name);
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent e) {
        PetriNet petriNet = PNEditor.getInstance().getDocument().getPetriNet();
        caseprob = 1;
        casetime = 0;
        casewaittime = 0;
        casetime = 0;
        for (Place place : petriNet.getPlaces()) {
            if (place.getIsStartPlace()) {
                start = place;
            }
        }
        for (Arc arc : petriNet.getArcs()) {
            arc.setArcTime(0);
            arc.setArcWaitTime(0);
        }
        dalsiP = start;
        lam = start.getArrivalRate();
        System.out.println(lam);
        while (!dalsiP.getIsEndPlace()) {
            if (dalsiP.getIsORSplit()) {
                startO = dalsiP;
                dalsiP = OR(startO, caseprob);
                lam = lam * dalsiP.getZmena();
            }
            if (dalsiP.getIsIterationStart()) {
                startO = dalsiP;
                dalsiP = ITER(startO, caseprob);
                for (Arc arc : dalsiP.getConnectedArcsNext()) {
                    if (arc.getIsIterationArc()) {
                        if (arc.getZmena()) {
                            lam = lam * (1 - arc.getChangedProbability());
                        } else {
                            lam = lam * (1 - arc.getProbability());
                        }
                    }
                }
            }
            if (dalsiP.getConnectedArcsNext().size() == 1) {
                dalsiT = dalsiP.getConnectedArcNext().getTransition();
            } else {
                for (Arc arc : dalsiP.getConnectedArcsNext()) {
                    if (!arc.getIsIterationArc()) {
                        dalsiT = arc.getTransition();
                    }
                }
            }
            if (dalsiT.getMM1()) {
                MM1(dalsiT, lam);
                dalsiT.SetProbability(caseprob);
                casetime += dalsiT.getSystemTime() * caseprob;
                casewaittime += dalsiT.getWaitingTime() * caseprob;
            }
            if (dalsiT.getMMc()) {
                MMc(dalsiT, lam);
                dalsiT.SetProbability(caseprob);
                casetime += dalsiT.getSystemTime() * caseprob;
                casewaittime += dalsiT.getWaitingTime() * caseprob;
            }
            if (dalsiT.getIsANDSplit()) {
                startA = dalsiT;
                dalsiT = AND(startA, caseprob);
            }
            System.out.println(dalsiT.getLabel().getText());
            dalsiP = dalsiT.getConnectedArcNext().getPlace();
            System.out.println("Nasleduje P");
            System.out.println(dalsiP.getLabel().getText());
        }
        System.out.println("System time je :" + casetime * 60);
        System.out.println("Waiting time je :" + casewaittime * 60);
        new OutPutTimeDialog(casetime * 60, casewaittime * 60, lam);
    }

    private Place OR(Place zaciatok, double inputprob) {
        Place koniecOR = new Place();
        double pomocna = 0;
        boolean zacinamOR;
        for (Arc arc : zaciatok.getConnectedArcsNext()) {
            dalsiP = zaciatok;
            zacinamOR = true;
            double probarc = inputprob * arc.getProbability();
            while (zaciatok.getORJoin() != dalsiP) {
                if (zacinamOR == true) {
                    dalsiT = arc.getTransition();
                    zacinamOR = false;
                    if (dalsiT.getMM1()) {
                        MM1(dalsiT, lam * probarc);
                        dalsiT.SetProbability(probarc);
                        casetime += dalsiT.getSystemTime() * probarc;
                        casewaittime += dalsiT.getWaitingTime() * probarc;
                    }
                    if (dalsiT.getMMc()) {
                        MMc(dalsiT, lam * probarc);
                        dalsiT.SetProbability(probarc);
                        casetime += dalsiT.getSystemTime() * probarc;
                        casewaittime += dalsiT.getWaitingTime() * probarc;
                    }
                } else {
                    if (dalsiP.getConnectedArcsNext().size() == 1) {
                        dalsiT = dalsiP.getConnectedArcNext().getTransition();
                    } else {
                        for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                            if (!arcc.getIsIterationArc()) {
                                dalsiT = arcc.getTransition();
                            }
                        }
                    }
                    if (dalsiT.getMM1()) {
                        MM1(dalsiT, lam * probarc);
                        dalsiT.SetProbability(probarc);
                        casetime += dalsiT.getSystemTime() * probarc;
                        casewaittime += dalsiT.getWaitingTime() * probarc;
                    }
                    if (dalsiT.getMMc()) {
                        MMc(dalsiT, lam * probarc);
                        dalsiT.SetProbability(probarc);
                        casetime += dalsiT.getSystemTime() * probarc;
                        casewaittime += dalsiT.getWaitingTime() * probarc;
                    }
                }
                System.out.println(dalsiT.getLabel().getText());
                if (dalsiT.getIsANDSplit()) {
                    startA = dalsiT;
                    dalsiT = AND(startA, probarc);
                    if (dalsiT.getMM1()) {
                        MM1(dalsiT, lam * probarc);
                        dalsiT.SetProbability(probarc);
                        casetime += dalsiT.getSystemTime() * probarc;
                        casewaittime += dalsiT.getWaitingTime() * probarc;
                    }
                    if (dalsiT.getMMc()) {
                        MMc(dalsiT, lam * probarc);
                        dalsiT.SetProbability(probarc);
                        casetime += dalsiT.getSystemTime() * probarc;
                        casewaittime += dalsiT.getWaitingTime() * probarc;
                    }
                }
                dalsiP = dalsiT.getConnectedArcNext().getPlace();
                if (dalsiP.getIsORSplit()) {
                    startO = dalsiP;
                    dalsiP = OR(startO, probarc);
                    probarc = dalsiP.getZmena();
                }
                if (dalsiP.getIsIterationStart()) {
                    startO = dalsiP;
                    dalsiP = ITER(startO, probarc);
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (arcc.getIsIterationArc()) {
                            probarc = probarc * (1 - arcc.getProbability());
                        }
                    }
                }
                System.out.println("Nasleduje P");
                System.out.println(dalsiP.getLabel().getText());
            }
            pomocna = pomocna + probarc;
            System.out.println(pomocna);
            System.out.println(casetime);
        }
        koniecOR = dalsiP;
        koniecOR.setZmena(pomocna);
        return koniecOR;
    }

    private Place OR(Place zaciatok, double inputprob, Arc timearc) {
        PetriNet petriNet = PNEditor.getInstance().getDocument().getPetriNet();
        Place koniecOR = new Place();
        double pomocna = 0;
        boolean zacinamOR;
        for (Arc arc : zaciatok.getConnectedArcsNext()) {
            dalsiP = zaciatok;
            zacinamOR = true;
            double probarc = inputprob * arc.getProbability();
            while (zaciatok.getORJoin() != dalsiP) {
                if (zacinamOR == true) {
                    dalsiT = arc.getTransition();
                    zacinamOR = false;
                    for (Arc arct : petriNet.getArcs()) {
                        if (arct.equals(timearc)) {
                            if (dalsiT.getMM1()) {
                                MM1(dalsiT, lam * probarc);
                                dalsiT.SetProbability(probarc);
                                arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarc));
                                arct.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * probarc));
                            }
                            if (dalsiT.getMMc()) {
                                MMc(dalsiT, lam * probarc);
                                dalsiT.SetProbability(probarc);
                                arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarc));
                                arct.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * probarc));
                            }
                            System.out.println("Tu tu tu" + arct.getArcTime());
                        }
                    }
                } else {
                    if (dalsiP.getConnectedArcsNext().size() == 1) {
                        dalsiT = dalsiP.getConnectedArcNext().getTransition();
                    } else {
                        for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                            if (!arcc.getIsIterationArc()) {
                                dalsiT = arcc.getTransition();
                            }
                        }
                    }
                    for (Arc arct : petriNet.getArcs()) {
                        if (arct.equals(timearc)) {
                            if (dalsiT.getMM1()) {
                                MM1(dalsiT, lam * probarc);
                                dalsiT.SetProbability(probarc);
                                arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarc));
                                arct.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * probarc));
                            }
                            if (dalsiT.getMMc()) {
                                MMc(dalsiT, lam * probarc);
                                dalsiT.SetProbability(probarc);
                                arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarc));
                                arct.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * probarc));
                            }
                        }
                    }
                }
                System.out.println(dalsiT.getLabel().getText());
                if (dalsiT.getIsANDSplit()) {
                    startA = dalsiT;
                    dalsiT = AND(startA, probarc, timearc);
                    for (Arc arct : petriNet.getArcs()) {
                        if (arct.equals(timearc)) {
                            if (dalsiT.getMM1()) {
                                MM1(dalsiT, lam * probarc);
                                dalsiT.SetProbability(probarc);
                                arct.setArcTime((arc.getArcTime() + dalsiT.getSystemTime() * probarc));
                                arct.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * probarc));
                            }
                            if (dalsiT.getMMc()) {
                                MMc(dalsiT, lam * probarc);
                                dalsiT.SetProbability(probarc);
                                arct.setArcTime((arc.getArcTime() + dalsiT.getSystemTime() * probarc));
                                arct.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * probarc));
                            }
                        }
                    }
                }
                dalsiP = dalsiT.getConnectedArcNext().getPlace();
                if (dalsiP.getIsORSplit()) {
                    startO = dalsiP;
                    dalsiP = OR(startO, probarc, timearc);
                    probarc = dalsiP.getZmena();
                }
                if (dalsiP.getIsIterationStart()) {
                    startO = dalsiP;
                    dalsiP = ITER(startO, probarc, timearc);
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (arcc.getIsIterationArc()) {
                            probarc = probarc * (1 - arcc.getProbability());
                        }
                    }
                }
                System.out.println("Nasleduje P");
                System.out.println(dalsiP.getLabel().getText());
            }
            pomocna += probarc;
        }
        koniecOR = dalsiP;
        koniecOR.setZmena(pomocna);
        return koniecOR;
    }

    private Transition AND(Transition zaciatok, double inputprob) {
        Transition koniecAND = new Transition();
        boolean zacinamAND;
        double pomocna = inputprob;
        System.out.println("Zaciatok andu : " + zaciatok.getLabel().getText());
        System.out.println("Koniec andu : " + zaciatok.getANDJoin().getLabel().getText());
        for (Arc arc : zaciatok.getConnectedArcsNext()) {
            dalsiT = zaciatok;
            zacinamAND = true;
            inputprob = pomocna;
            while (zaciatok.getANDJoin() != dalsiT) {
                if (zacinamAND == true) {
                    dalsiP = arc.getPlace();
                    zacinamAND = false;
                } else {
                    dalsiP = dalsiT.getConnectedArcNext().getPlace();
                }
                System.out.println(dalsiP.getLabel().getText());
                if (dalsiP.getIsORSplit()) {
                    startO = dalsiP;
                    dalsiP = OR(startO, inputprob, arc);
                    inputprob = dalsiP.getZmena();
                }
                if (dalsiP.getIsIterationStart()) {
                    startO = dalsiP;
                    dalsiP = ITER(startO, inputprob, arc);
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (arcc.getIsIterationArc()) {
                            inputprob = inputprob * (1 - arcc.getProbability());
                        }
                    }
                }
                if (dalsiP.getConnectedArcsNext().size() == 1) {
                    dalsiT = dalsiP.getConnectedArcNext().getTransition();
                } else {
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (!arcc.getIsIterationArc()) {
                            dalsiT = arcc.getTransition();
                        }
                    }
                }
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * inputprob);
                    dalsiT.SetProbability(inputprob);
                    arc.setArcTime((arc.getArcTime() + dalsiT.getSystemTime() * inputprob));
                    arc.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * inputprob));
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * inputprob);
                    dalsiT.SetProbability(inputprob);
                    arc.setArcTime((arc.getArcTime() + dalsiT.getSystemTime() * inputprob));
                    arc.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * inputprob));
                }
                if (dalsiT.getIsANDSplit()) {
                    startA = dalsiT;
                    dalsiT = AND(startA, inputprob, arc);
                }
                System.out.println("Nasleduje T");
                System.out.println(dalsiT.getLabel().getText());
            }
        }
        casetime += maxtime(zaciatok);
        casewaittime += maxwtime(zaciatok);
        koniecAND = dalsiT;
        return koniecAND;
    }

    private Transition AND(Transition zaciatok, double inputprob, Arc timearc) {
        PetriNet petriNet = PNEditor.getInstance().getDocument().getPetriNet();
        Transition koniecAND = new Transition();
        boolean zacinamAND;
        double pomocna = inputprob;
        for (Arc arc : zaciatok.getConnectedArcsNext()) {
            dalsiT = zaciatok;
            zacinamAND = true;
            inputprob = pomocna;
            while (zaciatok.getANDJoin() != dalsiT) {
                if (zacinamAND == true) {
                    dalsiP = arc.getPlace();
                    zacinamAND = false;
                } else {
                    dalsiP = dalsiT.getConnectedArcNext().getPlace();
                }
                System.out.println(dalsiP.getLabel().getText());
                if (dalsiP.getIsORSplit()) {
                    startO = dalsiP;
                    dalsiP = OR(startO, inputprob, arc);
                    inputprob = dalsiP.getZmena();
                }
                if (dalsiP.getIsIterationStart()) {
                    startO = dalsiP;
                    dalsiP = ITER(startO, inputprob, arc);
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (arcc.getIsIterationArc()) {
                            inputprob = inputprob * (1 - arcc.getProbability());
                        }
                    }
                }
                if (dalsiP.getConnectedArcsNext().size() == 1) {
                    dalsiT = dalsiP.getConnectedArcNext().getTransition();
                } else {
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (!arcc.getIsIterationArc()) {
                            dalsiT = arcc.getTransition();
                        }
                    }
                }
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * inputprob);
                    dalsiT.SetProbability(inputprob);
                    arc.setArcTime((arc.getArcTime() + dalsiT.getSystemTime() * inputprob));
                    arc.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * inputprob));
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * inputprob);
                    dalsiT.SetProbability(inputprob);
                    arc.setArcTime((arc.getArcTime() + dalsiT.getSystemTime() * inputprob));
                    arc.setArcWaitTime((arc.getArcWaitTime() + dalsiT.getWaitingTime() * inputprob));
                }
                if (dalsiT.getIsANDSplit()) {
                    startA = dalsiT;
                    dalsiT = AND(startA, inputprob, arc);
                }
                System.out.println("Nasleduje T");
                System.out.println(dalsiT.getLabel().getText());
            }
        }
        for (Arc arct : petriNet.getArcs()) {
            if (arct.equals(timearc)) {
                arct.setArcTime((arct.getArcTime() + maxtime(zaciatok)));
                arct.setArcWaitTime((arct.getArcWaitTime() + maxwtime(zaciatok)));
            }
        }
        koniecAND = dalsiT;
        return koniecAND;
    }

    private Place ITER(Place zaciatok, double inputprob) {
        Place koniecITER = new Place();
        boolean zacinamITER;
        boolean zacinamITERdown = false;
        dalsiP = zaciatok;
        zacinamITER = true;
        double upprob = 0;
        double downprob = 0;
        System.out.println("Zaciatok iter : " + zaciatok.getLabel().getText());
        System.out.println("Koniec iter : " + zaciatok.getIterationEnd().getLabel().getText());
        for (Arc arc : zaciatok.getIterationEnd().getConnectedArcsNext()) {
            if (arc.getIsIterationArc()) {
                upprob = 1 / (1 - arc.getProbability());
                downprob = arc.getProbability() / (1 - arc.getProbability());
            }
        }
        double probarcup = inputprob * upprob;
        double probarcdown = inputprob * downprob;
        while (zaciatok.getIterationEnd() != dalsiP) {
            if (zacinamITER == true) {
                dalsiT = zaciatok.getConnectedArcNext().getTransition();
                zacinamITER = false;
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * probarcup);
                    dalsiT.SetProbability(probarcup);
                    casetime += dalsiT.getSystemTime() * probarcup;
                    casewaittime += dalsiT.getWaitingTime() * probarcup;
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * probarcup);
                    dalsiT.SetProbability(probarcup);
                    casetime += dalsiT.getSystemTime() * probarcup;
                    casewaittime += dalsiT.getWaitingTime() * probarcup;
                }
            } else {
                if (dalsiP.getConnectedArcsNext().size() == 1) {
                    dalsiT = dalsiP.getConnectedArcNext().getTransition();
                } else {
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (!arcc.getIsIterationArc()) {
                            dalsiT = arcc.getTransition();
                        }
                    }
                }
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * probarcup);
                    dalsiT.SetProbability(probarcup);
                    casetime += dalsiT.getSystemTime() * probarcup;
                    casewaittime += dalsiT.getWaitingTime() * probarcup;
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * probarcup);
                    dalsiT.SetProbability(probarcup);
                    casetime += dalsiT.getSystemTime() * probarcup;
                    casewaittime += dalsiT.getWaitingTime() * probarcup;
                }
            }
            System.out.println(dalsiT.getLabel().getText());
            if (dalsiT.getIsANDSplit()) {
                startA = dalsiT;
                dalsiT = AND(startA, probarcup);
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * probarcup);
                    dalsiT.SetProbability(probarcup);
                    casetime += dalsiT.getSystemTime() * probarcup;
                    casewaittime += dalsiT.getWaitingTime() * probarcup;
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * probarcup);
                    dalsiT.SetProbability(probarcup);
                    casetime += dalsiT.getSystemTime() * probarcup;
                    casewaittime += dalsiT.getWaitingTime() * probarcup;
                }
            }
            dalsiP = dalsiT.getConnectedArcNext().getPlace();
            if (dalsiP.getIsORSplit()) {
                startO = dalsiP;
                dalsiP = OR(startO, probarcup);
                probarcup = dalsiP.getZmena();
            }
            if (dalsiP.getIsIterationStart()) {
                startO = dalsiP;
                dalsiP = ITER(startO, probarcup);
                for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                    if (arcc.getIsIterationArc()) {
                        if (arcc.getZmena()) {
                            probarcup = probarcup * (1 - arcc.getChangedProbability());
                            probarcdown = probarcdown * (1 - arcc.getChangedProbability());
                        } else {
                            probarcup = probarcup * (1 - arcc.getProbability());
                            probarcdown = probarcdown * (1 - arcc.getProbability());
                        }
                        for (Arc arc : zaciatok.getIterationEnd().getConnectedArcsNext()) {
                            if (arc.getIsIterationArc()) {
                                if (arcc.getZmena()) {
                                    arc.setChangedProbability(0);
                                    arc.setChangedProbability(arc.getProbability() * (1 - arcc.getChangedProbability()));
                                } else {
                                    arc.setZmena(true);
                                    arc.setChangedProbability(0);
                                    arc.setChangedProbability(arc.getProbability() * (1 - arcc.getProbability()));
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Nasleduje P");
            System.out.println(dalsiP.getLabel().getText());
        }
        System.out.println("Skoncil som hronu iter vetvu");
        zacinamITERdown = true;
        while (dalsiP != zaciatok) {
            System.out.println("Zacina dolnu iter vetvu");
            if (zacinamITERdown == true) {
                for (Arc arc : zaciatok.getIterationEnd().getConnectedArcsNext()) {
                    if (arc.getIsIterationArc()) {
                        dalsiT = arc.getTransition();
                        zacinamITERdown = false;
                    }
                }
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * probarcdown);
                    dalsiT.SetProbability(probarcdown);
                    casetime += dalsiT.getSystemTime() * probarcdown;
                    casewaittime += dalsiT.getWaitingTime() * probarcdown;
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * probarcdown);
                    dalsiT.SetProbability(probarcdown);
                    casetime += dalsiT.getSystemTime() * probarcdown;
                    casewaittime += dalsiT.getWaitingTime() * probarcdown;
                }
            } else {
                if (dalsiP.getConnectedArcsNext().size() == 1) {
                    dalsiT = dalsiP.getConnectedArcNext().getTransition();
                } else {
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (!arcc.getIsIterationArc()) {
                            dalsiT = arcc.getTransition();
                        }
                    }
                }
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * probarcdown);
                    dalsiT.SetProbability(probarcdown);
                    casetime += dalsiT.getSystemTime() * probarcdown;
                    casewaittime += dalsiT.getWaitingTime() * probarcdown;
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * probarcdown);
                    dalsiT.SetProbability(probarcdown);
                    casetime += dalsiT.getSystemTime() * probarcdown;
                    casewaittime += dalsiT.getWaitingTime() * probarcdown;
                }
            }
            if (dalsiT.getIsANDSplit()) {
                startA = dalsiT;
                dalsiT = AND(startA, probarcdown);
                if (dalsiT.getMM1()) {
                    MM1(dalsiT, lam * probarcdown);
                    dalsiT.SetProbability(probarcdown);
                    casetime += dalsiT.getSystemTime() * probarcdown;
                    casewaittime += dalsiT.getWaitingTime() * probarcdown;
                }
                if (dalsiT.getMMc()) {
                    MMc(dalsiT, lam * probarcdown);
                    dalsiT.SetProbability(probarcdown);
                    casetime += dalsiT.getSystemTime() * probarcdown;
                    casewaittime += dalsiT.getWaitingTime() * probarcdown;
                }
            }
            dalsiP = dalsiT.getConnectedArcNext().getPlace();
            if (dalsiP.getIsORSplit()) {
                startO = dalsiP;
                dalsiP = OR(startO, probarcdown);
                probarcdown = dalsiP.getZmena();
            }
            if (dalsiP.getIsIterationStart()) {
                if (dalsiP != zaciatok) {
                    startO = dalsiP;
                    dalsiP = ITER(startO, probarcdown);
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (arcc.getIsIterationArc()) {
                            if (arcc.getZmena()) {
                                probarcdown = probarcdown * (1 - arcc.getChangedProbability());
                            } else {
                                probarcdown = probarcdown * (1 - arcc.getProbability());
                            }
                        }
                    }
                }
            }
            System.out.println("Nasleduje P");
            System.out.println(dalsiP.getLabel().getText());
        }
        koniecITER = zaciatok.getIterationEnd();
        return koniecITER;
    }

    private Place ITER(Place zaciatok, double inputprob, Arc timearc) {
        PetriNet petriNet = PNEditor.getInstance().getDocument().getPetriNet();
        Place koniecITER = new Place();
        boolean zacinamITER;
        boolean zacinamITERdown = false;
        dalsiP = zaciatok;
        zacinamITER = true;
        double upprob = 0;
        double downprob = 0;
        for (Arc arc : zaciatok.getIterationEnd().getConnectedArcsNext()) {
            if (arc.getIsIterationArc()) {
                upprob = 1 / (1 - arc.getProbability());
                downprob = arc.getProbability() / (1 - arc.getProbability());
            }
        }
        double probarcup = inputprob * upprob;
        double probarcdown = inputprob * downprob;
        while (zaciatok.getIterationEnd() != dalsiP) {
            if (zacinamITER == true) {
                dalsiT = zaciatok.getConnectedArcNext().getTransition();
                zacinamITER = false;
                for (Arc arct : petriNet.getArcs()) {
                    if (arct.equals(timearc)) {
                        if (dalsiT.getMM1()) {
                            MM1(dalsiT, lam * probarcup);
                            dalsiT.SetProbability(probarcup);
                            arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarcup));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcup));
                        }
                        if (dalsiT.getMMc()) {
                            MMc(dalsiT, lam * probarcup);
                            dalsiT.SetProbability(probarcup);
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getSystemTime() * probarcup));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcup));
                        }
                    }
                }
            } else {
                if (dalsiP.getConnectedArcsNext().size() == 1) {
                    dalsiT = dalsiP.getConnectedArcNext().getTransition();
                } else {
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (!arcc.getIsIterationArc()) {
                            dalsiT = arcc.getTransition();
                        }
                    }
                }
                for (Arc arct : petriNet.getArcs()) {
                    if (arct.equals(timearc)) {
                        if (dalsiT.getMM1()) {
                            MM1(dalsiT, lam * probarcup);
                            dalsiT.SetProbability(probarcup);
                            arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarcup));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcup));
                        }
                        if (dalsiT.getMMc()) {
                            MMc(dalsiT, lam * probarcup);
                            dalsiT.SetProbability(probarcup);
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getSystemTime() * probarcup));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcup));
                        }
                    }
                }
            }
            System.out.println(dalsiT.getLabel().getText());
            if (dalsiT.getIsANDSplit()) {
                startA = dalsiT;
                dalsiT = AND(startA, probarcup);
                for (Arc arct : petriNet.getArcs()) {
                    if (arct.equals(timearc)) {
                        if (dalsiT.getMM1()) {
                            MM1(dalsiT, lam * probarcup);
                            dalsiT.SetProbability(probarcup);
                            arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarcup));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcup));
                        }
                        if (dalsiT.getMMc()) {
                            MMc(dalsiT, lam * probarcup);
                            dalsiT.SetProbability(probarcup);
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getSystemTime() * probarcup));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcup));
                        }
                    }
                }
            }
            dalsiP = dalsiT.getConnectedArcNext().getPlace();
            if (dalsiP.getIsORSplit()) {
                startO = dalsiP;
                dalsiP = OR(startO, probarcup);
                probarcup = dalsiP.getZmena();
            }
            if (dalsiP.getIsIterationStart()) {
                startO = dalsiP;
                dalsiP = ITER(startO, probarcup);
                for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                    if (arcc.getIsIterationArc()) {
                        if (arcc.getZmena()) {
                            probarcup = probarcup * (1 - arcc.getChangedProbability());
                            probarcdown = probarcdown * (1 - arcc.getChangedProbability());
                        } else {
                            probarcup = probarcup * (1 - arcc.getProbability());
                            probarcdown = probarcdown * (1 - arcc.getProbability());
                        }
                        for (Arc arc : zaciatok.getIterationEnd().getConnectedArcsNext()) {
                            if (arc.getIsIterationArc()) {
                                arc.setZmena(true);
                                if (arcc.getZmena()) {
                                    arc.setChangedProbability(0);
                                    arc.setChangedProbability(arc.getProbability() * (1 - arcc.getChangedProbability()));
                                } else {
                                    arc.setChangedProbability(0);
                                    arc.setChangedProbability(arc.getProbability() * (1 - arcc.getProbability()));
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Nasleduje P");
            System.out.println(dalsiP.getLabel().getText());
        }
        zacinamITERdown = true;
        while (zaciatok != dalsiP) {
            if (zacinamITERdown == true) {
                for (Arc arc : zaciatok.getIterationEnd().getConnectedArcsNext()) {
                    if (arc.getIsIterationArc()) {
                        dalsiT = arc.getTransition();
                        zacinamITERdown = false;
                    }
                }
                for (Arc arct : petriNet.getArcs()) {
                    if (arct.equals(timearc)) {
                        if (dalsiT.getMM1()) {
                            MM1(dalsiT, lam * probarcdown);
                            dalsiT.SetProbability(probarcdown);
                            arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarcdown));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcdown));
                        }
                        if (dalsiT.getMMc()) {
                            MMc(dalsiT, lam * probarcdown);
                            dalsiT.SetProbability(probarcdown);
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getSystemTime() * probarcdown));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcdown));
                        }
                    }
                }
            } else {
                if (dalsiP.getConnectedArcsNext().size() == 1) {
                    dalsiT = dalsiP.getConnectedArcNext().getTransition();
                } else {
                    for (Arc arcc : dalsiP.getConnectedArcsNext()) {
                        if (!arcc.getIsIterationArc()) {
                            dalsiT = arcc.getTransition();
                        }
                    }
                }
                for (Arc arct : petriNet.getArcs()) {
                    if (arct.equals(timearc)) {
                        if (dalsiT.getMM1()) {
                            MM1(dalsiT, lam * probarcdown);
                            dalsiT.SetProbability(probarcdown);
                            arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarcdown));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcdown));
                        }
                        if (dalsiT.getMMc()) {
                            MMc(dalsiT, lam * probarcdown);
                            dalsiT.SetProbability(probarcdown);
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getSystemTime() * probarcdown));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcdown));
                        }
                    }
                }
            }
            if (dalsiT.getIsANDSplit()) {
                startA = dalsiT;
                dalsiT = AND(startA, probarcdown, timearc);
                for (Arc arct : petriNet.getArcs()) {
                    if (arct.equals(timearc)) {
                        if (dalsiT.getMM1()) {
                            MM1(dalsiT, lam * probarcdown);
                            dalsiT.SetProbability(probarcdown);
                            arct.setArcTime((arct.getArcTime() + dalsiT.getSystemTime() * probarcdown));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcdown));
                        }
                        if (dalsiT.getMMc()) {
                            MMc(dalsiT, lam * probarcdown);
                            dalsiT.SetProbability(probarcdown);
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getSystemTime() * probarcdown));
                            arct.setArcWaitTime((arct.getArcWaitTime() + dalsiT.getWaitingTime() * probarcdown));
                        }
                    }
                }
            }
            dalsiP = dalsiT.getConnectedArcNext().getPlace();
            if (dalsiP.getIsORSplit()) {
                startO = dalsiP;
                dalsiP = OR(startO, probarcdown, timearc);
                probarcdown = dalsiP.getZmena();
            }
            if (dalsiP.getIsIterationStart()) {
                if (dalsiP != zaciatok) {
                    startO = dalsiP;
                    dalsiP = ITER(startO, probarcdown, timearc);
                    for (Arc arc : dalsiP.getConnectedArcsNext()) {
                        if (arc.getIsIterationArc()) {
                            if (arc.getZmena()) {
                                probarcdown = probarcdown * (1 - arc.getChangedProbability());
                            } else {
                                probarcdown = probarcdown * (1 - arc.getProbability());
                            }
                        }
                    }
                }
            }
        }
        koniecITER = zaciatok.getIterationEnd();
        return koniecITER;
    }

    private void MM1(Transition tran, Double lam) {
        double mi = 60 / tran.getServisRate();
        double utilization;
        double systime;
        double waittime;
        utilization = lam / mi;
        systime = 1 / (mi - lam);
        waittime = utilization / (mi - lam);
        tran.SetUtilization(utilization);
        tran.SetSystemTime(systime);
        tran.SetWaitingTime(waittime);
    }

    private void MMc(Transition tran, Double lam) {
        double mi = 60 / tran.getServisRate();
        int c = tran.getNumberOfServers();
        double utilization;
        double cro;
        double pnula;
        double erlang;
        double systime;
        double waittime;
        double suma = 0;
        utilization = lam / (mi * c);
        cro = c * utilization;
        for (int i = 1; i <= (c - 1); i++) {
            suma += (Math.pow(cro, i)) / factorial(i);
        }
        pnula = 1 / (1 + Math.pow(cro, c) / (factorial(c) * (1 - utilization)) + suma);
        erlang = (Math.pow(cro, c) * pnula) / (factorial(c) * (1 - utilization));
        systime = (1 / mi * (1 + erlang / (c * (1 - utilization))));
        waittime = erlang / (mi * c * (1 - utilization));
        tran.SetUtilization(utilization);
        tran.SetSystemTime(systime);
        tran.SetWaitingTime(waittime);
    }

    public static double factorial(int N) {
        double multi = 1;
        for (int i = 1; i <= N; i++) {
            multi = multi * i;
        }
        return multi;
    }

    public double maxtime(Transition tran) {
        double maxtime = 0;
        double pom;
        for (Arc arc : tran.getConnectedArcsNext()) {
            pom = arc.getArcTime();
            if (pom > maxtime) {
                maxtime = pom;
            }
        }
        return maxtime;
    }

    public double maxwtime(Transition tran) {
        double maxtime = 0;
        double maxwtime = 0;
        double pom;
        for (Arc arc : tran.getConnectedArcsNext()) {
            pom = arc.getArcTime();
            if (pom > maxtime) {
                maxtime = pom;
                maxwtime = arc.getArcWaitTime();
            }
        }
        return maxwtime;
    }
}
