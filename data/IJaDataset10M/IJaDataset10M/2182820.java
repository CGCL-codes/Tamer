package sma;

import sma.ontologia.Casella;
import sma.ontologia.Desastre;
import sma.ontologia.Estat;
import sma.ontologia.InfoPartida;
import sma.ontologia.Resposta;
import sma.ontologia.TipusSms;
import java.util.*;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.io.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.*;
import jade.content.*;
import jade.content.onto.*;
import jade.proto.ContractNetResponder;
import sma.ontologia.*;
import sma.Policia.RebreInformacio;
import sma.gui.*;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class bomber extends Agent {

    enum D {

        DALT, BAIX, ESQUERRA, DRETA
    }

    ;

    private AID coordBombers;

    private InfoPartida partida;

    private Posicio posicio = new Posicio();

    private Posicio pos_vella = new Posicio(1, 1);

    private Posicio posFinal = new Posicio();

    private D direccio;

    private Estat estat = Estat.PATRULLANT;

    LinkedList<Posicio> ruta;

    protected void takeDown() {
        System.out.println(getName() + ": Acabo la meva execuci�.");
    }

    /**
	   * Es mostra un missatge a l'�rea de logs de la GUI
	   * @param str String per mostrar
	   */
    private void showMessage(String str) {
        System.out.println(getLocalName() + ":" + str);
    }

    protected void setup() {
        this.setEnabledO2ACommunication(true, 1);
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType(UtilsAgents.AGENT_BOMBER);
        sd1.setName(getLocalName());
        sd1.setOwnership(UtilsAgents.OWNER);
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd1);
        dfd.setName(getAID());
        try {
            DFService.register(this, dfd);
            showMessage(getLocalName() + ": Registered to the DF");
            addBehaviour(new RebreInformacio());
        } catch (FIPAException e) {
            System.err.println(getLocalName() + " registration with DF " + "unsucceeded. Reason: " + e.getMessage());
            doDelete();
        }
    }

    public LinkedList calc_distancia(Posicio pinicial, Posicio pfinal) {
        LinkedList ruta = new LinkedList();
        List nova;
        LinkedList succesors = new LinkedList();
        boolean[][] caselles = new boolean[partida.getNumFiles()][partida.getNumCols()];
        Posicio p = new Posicio();
        Posicio novapos;
        ruta.add(pinicial);
        succesors.add(ruta);
        boolean fi = false;
        int i;
        for (int n = 0; n < partida.getNumFiles(); n++) {
            for (int m = 0; m < partida.getNumCols(); m++) {
                caselles[n][m] = true;
            }
        }
        caselles[pinicial.getFila()][pinicial.getColumna()] = false;
        while (succesors.isEmpty() == false && fi == false) {
            ruta = (LinkedList) succesors.poll();
            p = (Posicio) ruta.getLast();
            if (((Math.abs(p.getFila() - pfinal.getFila())) == 1 && p.getColumna() == pfinal.getColumna()) || ((Math.abs(p.getColumna() - pfinal.getColumna())) == 1 && p.getFila() == pfinal.getFila())) {
                fi = true;
            } else {
                for (i = 0; i < 4; i++) {
                    switch(i) {
                        case 0:
                            if (p.getFila() + 1 < partida.getNumFiles()) {
                                if (caselles[p.getFila() + 1][p.getColumna()] == true) {
                                    caselles[p.getFila() + 1][p.getColumna()] = false;
                                    if (partida.getCasella(p.getFila() + 1, p.getColumna()).casellaValida() == true) {
                                        nova = new LinkedList();
                                        nova = (LinkedList) ruta.clone();
                                        novapos = new Posicio(p.getFila() + 1, p.getColumna());
                                        nova.add(novapos);
                                        succesors.add(nova);
                                    }
                                }
                            }
                            break;
                        case 1:
                            if (p.getFila() - 1 >= 0) {
                                if (caselles[p.getFila() - 1][p.getColumna()] == true) {
                                    caselles[p.getFila() - 1][p.getColumna()] = false;
                                    if (partida.getCasella(p.getFila() - 1, p.getColumna()).casellaValida() == true) {
                                        nova = new LinkedList();
                                        nova = (LinkedList) ruta.clone();
                                        novapos = new Posicio(p.getFila() - 1, p.getColumna());
                                        nova.add(novapos);
                                        succesors.add(nova);
                                    }
                                }
                            }
                            break;
                        case 2:
                            if (p.getColumna() + 1 < partida.getNumCols()) {
                                if (caselles[p.getFila()][p.getColumna() + 1] == true) {
                                    caselles[p.getFila()][p.getColumna() + 1] = false;
                                    if (partida.getCasella(p.getFila(), p.getColumna() + 1).casellaValida() == true) {
                                        nova = new LinkedList();
                                        nova = (LinkedList) ruta.clone();
                                        novapos = new Posicio(p.getFila(), p.getColumna() + 1);
                                        nova.add(novapos);
                                        succesors.add(nova);
                                    }
                                }
                            }
                            break;
                        case 3:
                            if (p.getColumna() - 1 >= 0) {
                                if (caselles[p.getFila()][p.getColumna() - 1] == true) {
                                    caselles[p.getFila()][p.getColumna() - 1] = false;
                                    if (partida.getCasella(p.getFila(), p.getColumna() - 1).casellaValida() == true) {
                                        nova = new LinkedList();
                                        nova = (LinkedList) ruta.clone();
                                        novapos = new Posicio(p.getFila(), p.getColumna() - 1);
                                        nova.add(novapos);
                                        succesors.add(nova);
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        ruta.add(pfinal);
        return ruta;
    }

    public List<Posicio> cercaPosicions() {
        int x = 0;
        Random r = new Random();
        List<Posicio> llistaPosicions = new ArrayList<Posicio>();
        posicio = partida.getLlistaBomber(getLocalName());
        if (pos_vella.getFila() > posicio.getFila()) {
            direccio = D.BAIX;
            if (posicio.getFila() - 1 >= 0) {
                if (partida.getCasella(posicio.getFila() - 1, posicio.getColumna()).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila() - 1, posicio.getColumna()));
            }
        }
        if (pos_vella.getFila() < posicio.getFila()) {
            direccio = D.DALT;
            if (posicio.getFila() + 1 < partida.getNumFiles()) {
                if (partida.getCasella(posicio.getFila() + 1, posicio.getColumna()).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila() + 1, posicio.getColumna()));
            }
        }
        if (pos_vella.getColumna() > posicio.getColumna()) {
            direccio = D.ESQUERRA;
            if (posicio.getColumna() - 1 >= 0) {
                if (partida.getCasella(posicio.getFila(), posicio.getColumna() - 1).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila(), posicio.getColumna() - 1));
            }
        }
        if (pos_vella.getColumna() < posicio.getColumna()) {
            direccio = D.DRETA;
            if (posicio.getColumna() + 1 < partida.getNumCols()) {
                if (partida.getCasella(posicio.getFila(), posicio.getColumna() + 1).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila(), posicio.getColumna() + 1));
            }
        }
        x = r.nextInt() % 4;
        for (int i = 0; i < 4; i++) {
            switch(x) {
                case 0:
                    if (posicio.getFila() + 1 < partida.getNumFiles()) {
                        if (partida.getCasella(posicio.getFila() + 1, posicio.getColumna()).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila() + 1, posicio.getColumna()));
                    }
                    x = 1;
                    break;
                case 1:
                    if (posicio.getFila() - 1 >= 0) {
                        if (partida.getCasella(posicio.getFila() - 1, posicio.getColumna()).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila() - 1, posicio.getColumna()));
                    }
                    x = 2;
                    break;
                case 2:
                    if (posicio.getColumna() + 1 < partida.getNumCols()) {
                        if (partida.getCasella(posicio.getFila(), posicio.getColumna() + 1).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila(), posicio.getColumna() + 1));
                    }
                    x = 3;
                    break;
                case 3:
                    if (posicio.getColumna() - 1 >= 0) {
                        if (partida.getCasella(posicio.getFila(), posicio.getColumna() - 1).casellaValida()) llistaPosicions.add(new Posicio(posicio.getFila(), posicio.getColumna() - 1));
                    }
                    x = 0;
                    break;
            }
        }
        return llistaPosicions;
    }

    public Posicio cercaSeguentPosicio() {
        boolean trobat = false;
        Posicio p = null;
        posicio = partida.getLlistaBomber(getLocalName());
        Iterator it = ruta.listIterator();
        while (it.hasNext() && !trobat) {
            Posicio paux = (Posicio) it.next();
            if (paux.getFila() == posicio.getFila() && paux.getColumna() == posicio.getColumna()) {
                if (it.hasNext()) {
                    p = (Posicio) it.next();
                    trobat = true;
                }
            }
        }
        return p;
    }

    public void CercarAgents() {
        ServiceDescription sd = new ServiceDescription();
        if (coordBombers == null) {
            sd.setName("Agent Coordinador Bombers");
            coordBombers = UtilsAgents.searchAgent(this, sd);
        }
    }

    public class Patrullar extends OneShotBehaviour {

        List<Posicio> possiblesPos;

        Resposta resposta = new Resposta();

        public Patrullar() {
            resposta.setNomAgent(getLocalName());
            resposta.setAidAgent(getAID());
            resposta.setAccio(TipusAccio.Moures);
            CercarAgents();
        }

        public void action() {
            possiblesPos = cercaPosicions();
            pos_vella = partida.getLlistaBomber(getLocalName());
            resposta.setPosicions(possiblesPos);
            resposta.setActual(posicio);
            ACLMessage miss = new ACLMessage(ACLMessage.INFORM);
            enviarMissatge(TipusSms.Patrullar, resposta, coordBombers, miss);
        }
    }

    public class ApagarFoc extends OneShotBehaviour {

        Resposta resposta = new Resposta();

        List<Posicio> posicions = new ArrayList<Posicio>();

        List<Posicio> possiblesPos;

        public ApagarFoc() {
            resposta.setNomAgent(getLocalName());
            resposta.setAidAgent(getAID());
            resposta.setAccio(TipusAccio.Apagant);
            CercarAgents();
        }

        public void action() {
            boolean trobat = false;
            pos_vella = partida.getLlistaBomber(getLocalName());
            posicions.add(partida.getLlistaBomber(getLocalName()));
            ACLMessage miss = new ACLMessage(ACLMessage.INFORM);
            resposta.setPosicions(posicions);
            for (int i = 0; i < partida.getDesastres().size(); i++) {
                Desastre d = partida.getDesastre(i);
                Posicio p = ruta.getLast();
                if (d.getPosicio().getFila() == p.getFila() && d.getPosicio().getColumna() == p.getColumna()) {
                    resposta.setDesastre(d);
                    trobat = true;
                }
            }
            if (trobat) {
                enviarMissatge(TipusSms.Apagar, resposta, coordBombers, miss);
            } else {
                possiblesPos = cercaPosicions();
                pos_vella = partida.getLlistaBomber(getLocalName());
                resposta.setPosicions(possiblesPos);
                resposta.setActual(posicio);
                estat = Estat.PATRULLANT;
                resposta.setAccio(TipusAccio.Canviant);
                enviarMissatge(TipusSms.Patrullar, resposta, coordBombers, miss);
            }
        }
    }

    public class Ruta extends OneShotBehaviour {

        ArrayList<Posicio> seguentPosicio = new ArrayList<Posicio>();

        Resposta resposta = new Resposta();

        List<Posicio> possiblesPos;

        public Ruta() {
            resposta.setNomAgent(getLocalName());
            resposta.setAidAgent(getAID());
            CercarAgents();
        }

        public void action() {
            resposta.setAccio(TipusAccio.Moures);
            Posicio posSeguent = cercaSeguentPosicio();
            Casella c;
            int cremat = 0;
            c = partida.getCasella(ruta.getLast().getFila(), ruta.getLast().getColumna());
            try {
                cremat = (int) c.getCremat();
            } catch (Exception e) {
                System.out.print(e);
            }
            if (cremat == 99) {
                ACLMessage miss = new ACLMessage(ACLMessage.INFORM);
                possiblesPos = cercaPosicions();
                pos_vella = partida.getLlistaBomber(getLocalName());
                resposta.setPosicions(possiblesPos);
                resposta.setActual(posicio);
                estat = Estat.PATRULLANT;
                resposta.setAccio(TipusAccio.Canviant);
                enviarMissatge(TipusSms.Patrullar, resposta, coordBombers, miss);
            } else {
                Posicio posUltima = ruta.get(ruta.size() - 2);
                if (posSeguent.getFila() == posUltima.getFila() && posSeguent.getColumna() == posUltima.getColumna()) resposta.setAccio(TipusAccio.FiRuta);
                seguentPosicio.add(posSeguent);
                pos_vella = partida.getLlistaBomber(getLocalName());
                resposta.setPosicions(seguentPosicio);
                resposta.setActual(posicio);
                ACLMessage miss = new ACLMessage(ACLMessage.INFORM);
                enviarMissatge(TipusSms.Ruta, resposta, coordBombers, miss);
            }
        }
    }

    private void enviarMissatge(TipusSms tipusSms, Resposta resp, AID receptor, ACLMessage miss) {
        miss.addReceiver(receptor);
        miss.setSender(this.getAID());
        miss.setConversationId(tipusSms.toString());
        try {
            miss.setContentObject(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(miss);
    }

    class ConversaContractNet extends ContractNetResponder {

        Posicio posActual;

        Posicio posDesastre;

        public ConversaContractNet(Agent myAgent, MessageTemplate mt) {
            super(myAgent, mt);
        }

        protected ACLMessage prepareResponse(ACLMessage msg) {
            List<Posicio> llista = new ArrayList<Posicio>();
            Iterator it, itpos;
            ACLMessage reply = msg.createReply();
            try {
                this.posDesastre = (Posicio) msg.getContentObject();
                showMessage("Ha rebut una proposta de contracte, amb posicio desastre " + posDesastre.getFila() + ";" + posDesastre.getColumna());
                if (estat.equals(Estat.PATRULLANT)) {
                    ruta = new LinkedList<Posicio>();
                    reply.setPerformative(ACLMessage.PROPOSE);
                    posActual = partida.getLlistaBomber(getLocalName());
                    ruta = calc_distancia(posActual, posDesastre);
                    Iterator it1 = ruta.listIterator();
                    reply.setContentObject(ruta.size());
                } else {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContentObject(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return reply;
        }

        @Override
        protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
            showMessage(" rechazada ");
            addBehaviour(new RebreInformacio());
        }

        protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
            ACLMessage reply = accept.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setSender(getAID());
            reply.setProtocol(InteractionProtocol.FIPA_CONTRACT_NET);
            try {
                reply.setContentObject(ruta.getFirst());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ruta.size() == 2) {
                ParallelBehaviour c = new ParallelBehaviour();
                addBehaviour(c);
                c.addSubBehaviour(new RebreInformacio());
                showMessage("proposta acceptada, envio la ruta i canvio a estat Apagant");
                estat = Estat.APAGANTFOC;
                return reply;
            }
            ParallelBehaviour c = new ParallelBehaviour();
            addBehaviour(c);
            c.addSubBehaviour(new RebreInformacio());
            showMessage("proposta acceptada, envio la ruta");
            estat = Estat.ENRUTA;
            return reply;
        }

        protected void handleOutOfSequence(ACLMessage msg) {
            System.out.println(getLocalName() + ":" + msg.getSender().getLocalName() + "ha enviat un sms fora de la conversaci�");
        }
    }

    class RebreInformacio extends OneShotBehaviour {

        ACLMessage missatge;

        public void action() {
            while (missatge == null) {
                missatge = myAgent.receive();
            }
            if (missatge.getConversationId().equals(TipusSms.Patrullar.toString())) {
                try {
                    partida = (InfoPartida) missatge.getContentObject();
                    showMessage("Rebut del coordinador bombers el torn: TORN " + partida.getTorn() + " amb ordre de PATRULLAR");
                    addBehaviour(new Patrullar());
                    addBehaviour(new RebreInformacio());
                } catch (Exception e) {
                    System.out.println("Error rebent els torns de l'Agent Coordinador Bombers: " + e);
                }
            } else if (missatge.getConversationId().equals(TipusSms.Desastre.toString())) {
                try {
                    partida = (InfoPartida) missatge.getContentObject();
                    showMessage("Rebut del coordinador bombers una proposta per atendre un desastre: TORN " + partida.getTorn());
                    MessageTemplate temp = ContractNetResponder.createMessageTemplate(InteractionProtocol.FIPA_CONTRACT_NET);
                    addBehaviour(new ConversaContractNet(this.myAgent, temp));
                } catch (Exception e) {
                    System.out.println("Error rebent els torns de l'Agent Coordinador Bombers: " + e);
                }
            } else if (missatge.getConversationId().equals(TipusSms.Apagar.toString())) {
                try {
                    partida = (InfoPartida) missatge.getContentObject();
                    showMessage("Rebut del coordinador bombers una proposta per apagafar un foc: TORN " + partida.getTorn());
                    addBehaviour(new ApagarFoc());
                    addBehaviour(new RebreInformacio());
                } catch (Exception e) {
                    System.out.println("Error rebent els torns de l'Agent Coordinador Bombers: " + e);
                }
            } else if (missatge.getConversationId().equals(TipusSms.Ruta.toString())) {
                try {
                    partida = (InfoPartida) missatge.getContentObject();
                    showMessage("Rebut del coordinador bombers el torn: TORN " + partida.getTorn() + " amb ordre de RUTA");
                    addBehaviour(new Ruta());
                    addBehaviour(new RebreInformacio());
                } catch (Exception e) {
                    System.out.println("Error rebent els torns de l'Agent Coordinador Bombers: " + e);
                }
            } else {
                showMessage(getName() + ": Rebut missatge del coordinador bombers FI DE PARTIDA. ");
                doDelete();
            }
        }
    }
}
