package org.jcvi.glk.ctm.status.viralsequencingreport;

import org.jcvi.common.core.util.JoinedStringBuilder;
import org.jcvi.glk.helpers.FundingHelper;
import org.joda.time.DateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The projectAgreementRecords provides a storage independent method of passing
 * the information gathered about a project between the data gathering of
 * DBProjectAgreementRecords and the presentation code of
 * GscViralSequencingProjectStatusReport.
 */
public class ProjectAgreementRecord implements Comparable<ProjectAgreementRecord> {

    private Pattern mixedStringNumber = Pattern.compile("^([^0-9]+)([0-9]+)$");

    public final String id;

    public final boolean idIsNumber;

    public final String collaborator;

    public final String institution;

    public final String virusType;

    public final String studyObjective;

    public final int planned;

    public final int received;

    public final int inProgress;

    public final int discarded;

    public final int published;

    public final CollectionStatus status;

    public final DateTime endDate;

    public final boolean isCEIRS;

    public final Collection<FundingHelper.FundingSource> funding;

    public final boolean isContainsFlu;

    public ProjectAgreementRecord(String id, String collaborator, String institution, String virusType, String studyObjective, int planned, int received, int inProgress, int discarded, int published, CollectionStatus status, DateTime endDate, boolean isCEIRS, Collection<FundingHelper.FundingSource> fundingSource, boolean containsFlu) {
        this.id = id;
        this.idIsNumber = id.matches("^[0-9]+$");
        this.collaborator = collaborator;
        this.institution = institution;
        this.virusType = virusType;
        this.studyObjective = studyObjective;
        this.planned = planned;
        this.received = received;
        this.inProgress = inProgress;
        this.discarded = discarded;
        this.published = published;
        this.status = status;
        this.endDate = endDate;
        this.isCEIRS = isCEIRS;
        this.funding = fundingSource;
        this.isContainsFlu = containsFlu;
    }

    public String getFundingString() {
        funding.remove(FundingHelper.FundingSource.UNSPECIFIED_FUNDING_SOURCE);
        if (funding.isEmpty()) {
            return "";
        }
        Collection<String> names = new HashSet<String>();
        for (FundingHelper.FundingSource source : funding) {
            names.add(source.name());
        }
        return new JoinedStringBuilder(names).glue(",").build();
    }

    @Override
    public int compareTo(ProjectAgreementRecord o) {
        if (idIsNumber && o.idIsNumber) {
            return Integer.valueOf(id).compareTo(Integer.valueOf(o.id));
        }
        Matcher localPartNumber = mixedStringNumber.matcher(this.id);
        Matcher otherPartNumber = mixedStringNumber.matcher(o.id);
        if (localPartNumber.matches() && otherPartNumber.matches()) {
            String localStart = localPartNumber.group(1);
            String otherStart = otherPartNumber.group(1);
            String localNumber = localPartNumber.group(2);
            String otherNumber = otherPartNumber.group(2);
            if (localStart != null && localStart.equals(otherStart) && localNumber != null && otherNumber != null) {
                return Integer.valueOf(localNumber).compareTo(Integer.valueOf(otherNumber));
            }
        }
        return id.compareTo(o.id);
    }

    /**
     * The builder is only used from within the package. 
     * GscViralSequencingProjectStatusReport receives the data as 
     * ProjectAgreementRecord objects.
     */
    static class Builder implements org.jcvi.common.core.util.Builder<ProjectAgreementRecord> {

        private final String id;

        public final Set<FundingHelper.FundingSource> funding;

        public final Set<String> collaborators = new LinkedHashSet<String>();

        public final Set<String> institutions = new LinkedHashSet<String>();

        public final List<VirusType> virusTypes = new ArrayList<VirusType>();

        public Set<String> studyObjectives = new LinkedHashSet<String>();

        public int planned = 0;

        public int received = 0;

        public int inProgress = 0;

        public int published = 0;

        public int discarded = 0;

        public CollectionStatus status = CollectionStatus.NOT_STARTED;

        public DateTime endDate = null;

        public boolean isCEIRS = false;

        Builder(String id, VirusType type) {
            this.id = id;
            virusTypes.add(type);
            this.funding = EnumSet.noneOf(FundingHelper.FundingSource.class);
        }

        Builder addCollaborator(String collaborator) {
            collaborators.add(collaborator);
            return this;
        }

        Builder addInstitution(String institution) {
            institutions.add(institution);
            return this;
        }

        Builder addStudyObjective(String objective) {
            studyObjectives.add(objective);
            return this;
        }

        Builder addPlannedSamples(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count can not be <0 : " + count);
            }
            planned += count;
            return this;
        }

        Builder addReceived(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count can not be <0 : " + count);
            }
            received += count;
            return this;
        }

        Builder addInProgress(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count can not be <0 : " + count);
            }
            inProgress += count;
            return this;
        }

        Builder addPublished(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count can not be <0 : " + count);
            }
            published += count;
            return this;
        }

        Builder addDiscarded(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("count can not be <0 : " + count);
            }
            discarded += count;
            return this;
        }

        Builder endDate(DateTime endDate) {
            if (this.endDate == null || this.endDate.isBefore(endDate)) {
                this.endDate = endDate;
            }
            return this;
        }

        Builder isCEIRS() {
            this.isCEIRS = true;
            return this;
        }

        Builder addCollectionStatus(CollectionStatus status) {
            this.status = this.status.updateStatus(status);
            return this;
        }

        Builder addFundingSources(EnumSet<FundingHelper.FundingSource> sources) {
            if (sources != null) {
                funding.addAll(sources);
            }
            return this;
        }

        @Override
        public ProjectAgreementRecord build() {
            if (virusTypes.isEmpty()) {
                virusTypes.add(VirusType.UNKNOWN);
            }
            EnumSet<VirusType> virusTypeSet = EnumSet.copyOf(virusTypes);
            return new ProjectAgreementRecord(id, new JoinedStringBuilder(this.collaborators).glue("/").build(), new JoinedStringBuilder(this.institutions).glue("/").build(), new JoinedStringBuilder(virusTypeSet).glue("/").build(), new JoinedStringBuilder(studyObjectives).glue("/").build(), planned, received, inProgress, discarded, published, status, endDate, isCEIRS, funding, VirusType.containsFlu(virusTypeSet));
        }
    }
}
