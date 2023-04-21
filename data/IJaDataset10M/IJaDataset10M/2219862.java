package sma;

import java.util.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.io.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import jade.util.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.*;
import jade.content.*;
import jade.content.onto.*;
import jade.proto.ContractNetInitiator;
import sma.ontologia.*;
import sma.gui.*;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class AgentCoordBombers extends Agent {

    private List<AID> llistaBombers = new ArrayList<AID>();

    private AID agentCoord;

    private InfoPartida partida;

    private Hashtable novesPosicions;

    private List<Resposta> respostes;

    private Hashtable estats = new Hashtable();

    private Hashtable ordres = new Hashtable();

    private ArrayList<Desastre> llistaDesastres = new ArrayList<Desastre>();

    private ArrayList<Desastre> DesastresNoAtesos = new ArrayList<Desastre>();

    private boolean contract_net = false;

    protected void takeDown() {
        System.out.println("coord bombers: Acabo la meva execuci�.");
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
        sd1.setType(UtilsAgents.AGENT_COORDINADOR_BOMBERS);
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

    /*************************************************************************/
    public void CercarAgents() {
        ServiceDescription sd = new ServiceDescription();
        AID aid;
        if (agentCoord == null) {
            sd.setName("coord");
            agentCoord = UtilsAgents.searchAgent(this, sd);
        }
    }

    public class EnviarInformacio extends OneShotBehaviour {

        TipusSms tipusSms;

        TipusDestinatari receptors;

        ServiceDescription sd = new ServiceDescription();

        int id;

        Resposta r;

        public EnviarInformacio(TipusDestinatari receptors, TipusSms tipusSms) {
            this.receptors = receptors;
            this.tipusSms = tipusSms;
        }

        public EnviarInformacio(TipusDestinatari receptors, TipusSms tipusSms, int id) {
            this.receptors = receptors;
            this.tipusSms = tipusSms;
            this.id = id;
        }

        public void action() {
            ACLMessage informSms = new ACLMessage(ACLMessage.INFORM);
            AID aid;
            CercarAgents();
            switch(receptors) {
                case Bombers:
                    for (int i = 0; i < partida.getNumBombers(); i++) {
                        sd.setName("Agent Bomber " + i);
                        aid = UtilsAgents.searchAgent(this.myAgent, sd);
                        informSms.addReceiver(aid);
                    }
                    break;
                case Coordinador:
                    informSms.addReceiver(agentCoord);
                    break;
                case Bomber:
                    sd.setName("Agent Bomber " + id);
                    aid = UtilsAgents.searchAgent(this.myAgent, sd);
                    informSms.addReceiver(aid);
                    break;
            }
            informSms.setSender(getAID());
            switch(tipusSms) {
                case FiPartida:
                    showMessage("Envio informaci� FI DE LA PARTIDA als bombers");
                    break;
                case Patrullar:
                    showMessage("Envio informaci� (torn: " + partida.getTorn() + " ordre: " + tipusSms + ") a l'agent bomber " + id);
                    break;
                case Torn:
                    showMessage("Envio informaci� (torn: " + partida.getTorn() + ", timeout: " + partida.getTimeout() + ") als agents bombers");
                    break;
                case Resposta:
                    showMessage("Envio resposta (torn: " + partida.getTorn() + ", timeout: " + partida.getTimeout() + ") al l'agent Coordinador");
                    r = new Resposta();
                    r.setLlistaPosicions(novesPosicions);
                    r.setLlistaEstats(estats);
                    r.setLlistaDesastres(llistaDesastres);
                    break;
                case Ruta:
                    showMessage("Envio informacio (torn: " + partida.getTorn() + ", timeout: " + partida.getTimeout() + ") al l'agent bomber" + id);
                    break;
                case Desastre:
                    showMessage("Informo d'un proper contract-net als bombers");
                    break;
            }
            try {
                informSms.setConversationId(tipusSms.toString());
                if (tipusSms.equals(tipusSms.Resposta)) informSms.setContentObject(r); else informSms.setContentObject(partida);
            } catch (IOException e) {
                e.printStackTrace();
            }
            send(informSms);
        }
    }

    public boolean pucPosicionar(Posicio pos) {
        Posicio p;
        int numagents = 0;
        Enumeration e = novesPosicions.elements();
        while (e.hasMoreElements()) {
            p = (Posicio) e.nextElement();
            if (p.getFila() == pos.getFila() && p.getColumna() == pos.getColumna()) numagents++;
        }
        if (numagents + partida.getCasella(pos.getFila(), pos.getColumna()).getNumAgents() < partida.getCasella(pos.getFila(), pos.getColumna()).getMaxAgents()) return true; else return false;
    }

    public String tincMesPrioritat(Posicio pos, String agent) {
        Posicio p;
        String clau;
        String clauAgent = null;
        Enumeration e = novesPosicions.keys();
        while (e.hasMoreElements() && clauAgent != null) {
            clau = (String) e.nextElement();
            p = (Posicio) novesPosicions.get(clau);
            if ((p.getFila() == pos.getFila() && p.getColumna() == pos.getColumna())) {
                switch((Estat) estats.get(agent)) {
                    case ENRUTA:
                        if ((Estat) estats.get(clau) == Estat.PATRULLANT) clauAgent = clau;
                        break;
                    case APAGANTFOC:
                        clauAgent = clau;
                        break;
                }
            }
        }
        return clauAgent;
    }

    public void resoldreConflictes() {
        Resposta r;
        Iterator it, itpos;
        boolean posicionat;
        Posicio p;
        it = respostes.listIterator();
        while (it.hasNext()) {
            r = (Resposta) it.next();
            itpos = r.getPosicions().listIterator();
            posicionat = false;
            while (!posicionat && itpos.hasNext()) {
                p = (Posicio) itpos.next();
                if (pucPosicionar(p)) {
                    System.out.println(r.getNomAgent() + ": ocupo nova posici� " + p.getFila() + "," + p.getColumna());
                    novesPosicions.put(r.getNomAgent(), new Posicio(p.getFila(), p.getColumna()));
                    posicionat = true;
                } else {
                    String clau = tincMesPrioritat(p, r.getNomAgent());
                    if (clau != null) {
                        novesPosicions.remove(clau);
                        Posicio pos = (Posicio) partida.getLlistaBombers().get(clau);
                        novesPosicions.put(clau, new Posicio(pos.getFila(), pos.getColumna()));
                        novesPosicions.put(r.getNomAgent(), new Posicio(p.getFila(), p.getColumna()));
                        posicionat = true;
                    }
                }
            }
            if (!posicionat) {
                Posicio pos = (Posicio) partida.getLlistaBombers().get(r.getNomAgent());
                novesPosicions.put(r.getNomAgent(), new Posicio(pos.getFila(), pos.getColumna()));
            } else if (r.getAccio() == TipusAccio.FiRuta) {
                estats.put(r.getNomAgent(), Estat.APAGANTFOC);
            }
        }
    }

    class RebreInformacioBombers extends OneShotBehaviour {

        ACLMessage missatge;

        TipusSms ordre;

        Resposta resposta;

        int num_agents;

        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            respostes = new ArrayList<Resposta>();
            novesPosicions = new Hashtable();
            contract_net = false;
            num_agents = partida.getNumBombers();
            contract_net = false;
            llistaDesastres.clear();
            while (respostes.size() < num_agents) {
                missatge = myAgent.receive(mt);
                if (missatge != null) {
                    try {
                        resposta = (Resposta) missatge.getContentObject();
                        if (resposta.getAccio().equals(TipusAccio.Canviant)) {
                            estats.put(resposta.getNomAgent(), Estat.PATRULLANT);
                        }
                        showMessage("Ha rebut resposta de " + resposta.getNomAgent() + " amb ACCI� " + resposta.getAccio());
                        if (resposta.getDesastre() != null) {
                            llistaDesastres.add(resposta.getDesastre());
                        }
                        respostes.add(resposta);
                    } catch (Exception e) {
                        System.out.println("Error rebent informaci� dels Bombers: " + e);
                    }
                } else {
                    block();
                }
            }
            resoldreConflictes();
            addBehaviour(new EnviarInformacio(TipusDestinatari.Coordinador, TipusSms.Resposta));
            addBehaviour(new RebreInformacio());
        }
    }

    class conversaContractNet extends ContractNetInitiator {

        Integer millorProposta = null;

        public conversaContractNet(Agent myAgent, ACLMessage sms) {
            super(myAgent, sms);
            showMessage("Inicio proposta CONTRACTNET.");
        }

        protected void handlePropose(ACLMessage propose, Vector v) {
            Integer i = -1;
            try {
                i = (Integer) propose.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            showMessage("Agent " + propose.getSender().getName() + " proposa cam� m�nim amb cost " + i);
        }

        protected void handleRefuse(ACLMessage refuse) {
            showMessage("Agent " + refuse.getSender().getName() + " refusa la proposta d'atendre el desastre");
        }

        protected void handleFailure(ACLMessage failure) {
            if (failure.getSender().equals(myAgent.getAMS())) {
                System.out.println("Responder no existeix not exist");
            } else {
                System.out.println("Agent " + failure.getSender().getName() + " ha fallat");
            }
        }

        protected void handleAllResponses(Vector responses, Vector acceptances) {
            showMessage("Rebudes totes les respostes. Num respostes totals:" + responses.size());
            AID AIDmillorProposta = null;
            ACLMessage accept = null;
            Enumeration e = responses.elements();
            while (e.hasMoreElements()) {
                ACLMessage msg = (ACLMessage) e.nextElement();
                if (msg.getPerformative() == ACLMessage.PROPOSE) {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.addElement(reply);
                    try {
                        Integer proposta = (Integer) msg.getContentObject();
                        if (millorProposta == null || proposta.compareTo(millorProposta) < 0) {
                            millorProposta = proposta;
                            AIDmillorProposta = msg.getSender();
                            accept = reply;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (accept != null && millorProposta != 1000) {
                showMessage("Aceptem proposta de " + millorProposta + " del bomber " + AIDmillorProposta.getName());
                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            }
        }

        protected void handleInform(ACLMessage inform) {
            TipusSms ordre;
            showMessage("Agent " + inform.getSender().getLocalName() + " atendr� el desastre");
            try {
                Posicio p = (Posicio) inform.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            if (millorProposta.intValue() == 2) {
                estats.put(inform.getSender().getLocalName(), Estat.APAGANTFOC);
            } else {
                estats.put(inform.getSender().getLocalName(), Estat.ENRUTA);
            }
            Enumeration e = estats.keys();
            while (e.hasMoreElements()) {
                String clau = (String) e.nextElement();
            }
            EnviarSms();
            contract_net = true;
            addBehaviour(new RebreInformacioBombers());
        }
    }

    public void EnviarSms() {
        TipusSms ordre = null;
        for (int j = 0; j < partida.getNumBombers(); j++) {
            switch((Estat) estats.get("Agent Bomber " + j)) {
                case ENRUTA:
                    ordre = TipusSms.Ruta;
                    break;
                case PATRULLANT:
                    ordre = TipusSms.Patrullar;
                    break;
                case APAGANTFOC:
                    ordre = TipusSms.Apagar;
                    break;
            }
            addBehaviour(new EnviarInformacio(TipusDestinatari.Bomber, ordre, j));
        }
    }

    class RebreInformacio extends OneShotBehaviour {

        ACLMessage missatge;

        TipusSms ordre;

        public void action() {
            MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            MessageTemplate mt2 = MessageTemplate.MatchContent(TipusSms.Torn.toString());
            MessageTemplate mt3 = MessageTemplate.or(mt2, MessageTemplate.MatchContent(TipusSms.FiPartida.toString()));
            MessageTemplate mt = MessageTemplate.and(mt1, mt3);
            while (missatge == null) {
                missatge = myAgent.receive();
            }
            if (missatge.getConversationId().equals(TipusSms.Torn.toString())) {
                try {
                    partida = (InfoPartida) missatge.getContentObject();
                    showMessage("Ha rebut del coordinador el torn: TORN " + partida.getTorn());
                    if (partida.getTorn() == 1) {
                        for (int i = 0; i < partida.getNumBombers(); i++) {
                            estats.put("Agent Bomber " + i, Estat.PATRULLANT);
                        }
                    }
                    ordre = seleccionarOrdres();
                    switch(ordre) {
                        case Moures:
                            for (int j = 0; j < partida.getNumBombers(); j++) {
                                addBehaviour(new EnviarInformacio(TipusDestinatari.Bomber, (TipusSms) ordres.get("Agent Bomber " + j), j));
                            }
                            addBehaviour(new RebreInformacioBombers());
                            break;
                        case Desastre:
                            Posicio incendi = new Posicio();
                            addBehaviour(new EnviarInformacio(TipusDestinatari.Bombers, ordre));
                            ACLMessage sms = new ACLMessage(ACLMessage.CFP);
                            sms.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
                            sms.setConversationId(TipusSms.Desastre.toString());
                            CercarAgents();
                            ServiceDescription sd = new ServiceDescription();
                            AID aid;
                            for (int i = 0; i < partida.getNumBombers(); i++) {
                                sd.setName("Agent Bomber " + i);
                                aid = UtilsAgents.searchAgent(this.myAgent, sd);
                                sms.addReceiver(aid);
                                Estat estat = (Estat) estats.get("Agent Bomber " + i);
                            }
                            if (!DesastresNoAtesos.isEmpty()) {
                                incendi = DesastresNoAtesos.get(0).getPosicio();
                                DesastresNoAtesos.remove(0);
                                if (partida.hihaNouIncendi() == true) {
                                    DesastresNoAtesos.add(partida.nouIncedi());
                                }
                            } else {
                                incendi = partida.nouIncedi().getPosicio();
                            }
                            sms.setContentObject(new Posicio(incendi.getFila(), incendi.getColumna()));
                            addBehaviour(new conversaContractNet(this.myAgent, sms));
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error rebent els torns de l'Agent Coordinador: " + e);
                }
            } else {
                showMessage("Rebut missatge del coordinador FI DE PARTIDA. ");
                addBehaviour(new EnviarInformacio(TipusDestinatari.Bombers, TipusSms.FiPartida));
            }
        }
    }

    public TipusSms seleccionarOrdres() {
        TipusSms ordre = null;
        Enumeration e = estats.keys();
        boolean iniciar = false;
        while (e.hasMoreElements()) {
            String clau = (String) e.nextElement();
            if (estats.get(clau).equals(Estat.PATRULLANT)) iniciar = true;
        }
        if ((iniciar == false) && (partida.hihaNouIncendi())) {
            DesastresNoAtesos.add(partida.nouIncedi());
        }
        if ((partida.hihaNouIncendi() || DesastresNoAtesos.isEmpty() != true) && iniciar) {
            return TipusSms.Desastre;
        } else {
            for (int i = 0; i < partida.getNumBombers(); i++) {
                switch((Estat) estats.get("Agent Bomber " + i)) {
                    case ENRUTA:
                        ordre = TipusSms.Ruta;
                        break;
                    case PATRULLANT:
                        ordre = TipusSms.Patrullar;
                        break;
                    case APAGANTFOC:
                        ordre = TipusSms.Apagar;
                        break;
                }
                ordres.put("Agent Bomber " + i, ordre);
            }
            return TipusSms.Moures;
        }
    }
}
