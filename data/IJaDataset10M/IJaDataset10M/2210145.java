package de.uni_leipzig.lots.common.objects;

import org.quartz.JobDetail;
import org.jetbrains.annotations.NotNull;
import java.util.Date;

/**
 * @author Alexander Kiel
 * @version $Id: LotsJobDetail.java,v 1.5 2007/10/23 06:29:22 mai99bxd Exp $
 */
public class LotsJobDetail implements Entity<String> {

    private final JobDetail jobDetail;

    @NotNull
    private final Date creation;

    @NotNull
    private final Date lastChange;

    public LotsJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
        this.creation = new Date();
        this.lastChange = new Date();
    }

    @NotNull
    public Class<LotsJobDetail> getEntityType() {
        return LotsJobDetail.class;
    }

    @NotNull
    public String getId() {
        return jobDetail.getGroup() + ":" + jobDetail.getName();
    }

    public String getGroup() {
        return jobDetail.getGroup();
    }

    public String getName() {
        return jobDetail.getName();
    }

    @NotNull
    public Date getCreation() {
        return creation;
    }

    @NotNull
    public Date getLastChange() {
        return lastChange;
    }
}
