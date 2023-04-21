package herschel.phs.prophandler.tools.missionevolver.data.event;

public class EndEvolutionEvent extends EvolutionEvent {

    public String m_progamme;

    public String m_proposal;

    public String m_mode;

    public String m_missionConfig;

    public EndEvolutionEvent(String progamme, String proposal, String mode, String config) {
        m_progamme = progamme;
        m_proposal = proposal;
        m_mode = mode;
        m_missionConfig = config;
    }

    public String getMissionConfig() {
        return m_missionConfig;
    }

    public String getMode() {
        return m_mode;
    }

    public String getProgamme() {
        return m_progamme;
    }

    public String getProposal() {
        return m_proposal;
    }
}
