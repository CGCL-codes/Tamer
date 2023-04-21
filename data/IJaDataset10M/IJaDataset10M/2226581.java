package net.sf.qagesa.agents.services.impl;

import eduni.simjava.Sim_event;
import gridsim.GridSimTags;
import gridsim.IO_data;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.agents.services.PlatformService;
import net.sf.qagesa.agents.services.impl.mum.MuMRepository;
import net.sf.qagesa.constants.QAGESATags;
import net.sf.qagesa.grid.components.QAGESAGridElement;
import net.sf.qagesa.messages.MuMSearchReply;
import net.sf.qagesa.messages.MuMSearchRequest;
import net.sf.qagesa.multimedia.TranscodingSet;

/**
 * MuM service
 * 
 * 
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.services.PlatformService
 */
public class MuMService extends PlatformService {

    private static TranscodingSet transcodingSet;

    public static TranscodingSet getMUMTranscodingSet() {
        return MuMService.transcodingSet;
    }

    private MuMRepository repository;

    /**
	 * @param ap
	 * @param trace_flag
	 * @throws Exception
	 */
    public MuMService(AgentPlatform ap, boolean trace_flag) throws Exception {
        super(ap, "MuM", trace_flag);
        this.setRepository(new MuMRepository());
    }

    public void createAndStoreAllMovies() {
        this.setTranscodingSet(new TranscodingSet("measures/videos.csv", "measures/chunks.csv"));
        for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization().getNumSEs(); i++) {
            QAGESAGridElement se = (QAGESAGridElement) this.getAgentPlatform().getVirtualOrganization().getSEs().get(i);
            for (int j = 0; j < this.getTranscodingSet().size(); j++) {
                String movieTag = (String) this.getTranscodingSet().keySet().toArray()[j];
                this.getRepository().put(movieTag, se.get_id());
                se.addSequence(movieTag, this.getTranscodingSet().get(movieTag));
            }
        }
    }

    @Override
    public void initialize() throws Exception {
        super.initialize();
        this.createAndStoreAllMovies();
    }

    @Override
    protected void dispose() {
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
        switch(ev.get_tag()) {
            case QAGESATags.CEL_SEARCH_REP:
                break;
            case QAGESATags.MUM_SEARCH_REQ:
                MuMSearchRequest searchRequest = MuMSearchRequest.get_data(ev);
                int SIZE = 500;
                int replyToID = 0;
                replyToID = searchRequest.getSrc_ID();
                MuMSearchReply searchReply = new MuMSearchReply(ev.get_tag(), true, searchRequest, this.getRepository().getGEList(searchRequest.getMovieTag()));
                if (searchReply.getGelist() != null) {
                    searchReply.setOk(true);
                } else {
                    searchReply.setOk(false);
                }
                super.send(super.output, GridSimTags.SCHEDULE_NOW, QAGESATags.MUM_SEARCH_REP, new IO_data(searchReply, SIZE, replyToID));
                break;
            default:
                break;
        }
    }

    public TranscodingSet getTranscodingSet() {
        return transcodingSet;
    }

    public void setTranscodingSet(TranscodingSet transcodingSet) {
        MuMService.transcodingSet = transcodingSet;
    }

    public MuMRepository getRepository() {
        return repository;
    }

    public void setRepository(MuMRepository repository) {
        this.repository = repository;
    }
}
