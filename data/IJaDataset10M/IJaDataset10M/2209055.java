package net.conquiris.api.index;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import net.derquinse.common.meta.Metas;
import net.derquinse.common.meta.Metas.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

/**
 * Conquiris index report.
 * @author Andres Rodriguez
 */
@Immutable
public final class IndexReport implements IndexStatusProperty, IndexStartedFlag, IndexActiveFlag {

    /** Report level. */
    private final IndexReportLevel level;

    /** Whether the index is started. */
    private final boolean started;

    /** Whether the index is active (must be started). */
    private final boolean active;

    /** Last known index status. */
    private final IndexStatus status;

    /** Index delays. Not included in basic reports. */
    private final Optional<Delays> delays;

    /** Index info. Not included in basic reports. */
    private final Optional<IndexInfo> info;

    /**
	 * Creates a new basic report.
	 * @param started Whether the index is started.
	 * @param active Whether the index is active. If started is false it will be set to false.
	 * @param status Last known index status.
	 * @return The created report.
	 */
    public static IndexReport basic(boolean started, boolean active, IndexStatus status) {
        return new IndexReport(IndexReportLevel.BASIC, started, active, status, null, null);
    }

    /**
	 * Creates a new normal report.
	 * @param started Whether the index is started.
	 * @param active Whether the index is active. If started is false it will be set to false.
	 * @param status Last known index status.
	 * @param delays Index delay configuration.
	 * @param info Index info.
	 * @return The created report.
	 */
    public static IndexReport normal(boolean started, boolean active, IndexStatus status, Delays delays, IndexInfo info) {
        return new IndexReport(IndexReportLevel.NORMAL, started, active, status, delays, info);
    }

    /**
	 * Creates a new detailed report.
	 * @param started Whether the index is started.
	 * @param active Whether the index is active. If started is false it will be set to false.
	 * @param status Last known index status.
	 * @param delays Index delay configuration.
	 * @param info Index info.
	 * @return The created report.
	 */
    public static IndexReport detailed(boolean started, boolean active, IndexStatus status, Delays delays, IndexInfo info) {
        return new IndexReport(IndexReportLevel.DETAILED, started, active, status, delays, info);
    }

    /** Constructor. */
    IndexReport(IndexReportLevel level, boolean started, boolean active, IndexStatus status, @Nullable Delays delays, @Nullable IndexInfo info) {
        this.level = checkNotNull(level, "The index report level must be provided");
        this.status = checkNotNull(status, "The index status must be provided");
        this.started = started;
        this.active = active && started;
        if (level == IndexReportLevel.BASIC) {
            this.delays = Optional.absent();
            this.info = Optional.absent();
        } else {
            this.delays = Optional.of(checkNotNull(delays, "The index delay configuration must be provided"));
            if (level == IndexReportLevel.NORMAL) {
                info = checkNotNull(info, "The index information must be provided").asBasic();
            }
            this.info = Optional.of(info);
        }
    }

    /** Returns the report level. */
    public IndexReportLevel getLevel() {
        return level;
    }

    public IndexStatus getIndexStatus() {
        return status;
    }

    public boolean isIndexStarted() {
        return started;
    }

    public boolean isIndexActive() {
        return active;
    }

    /** Returns the index delay configuration. Absent in basic reports. */
    public Optional<Delays> getDelays() {
        return delays;
    }

    /**
	 * Return the index info. Absent in basic reports and user properties included only in detailed
	 * reports.
	 */
    public Optional<IndexInfo> getInfo() {
        return info;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(level, started, isIndexActive(), status, delays, info);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IndexReport) {
            IndexReport other = (IndexReport) obj;
            return this.level == other.level && this.started == other.started && this.active == other.active && this.status == other.status && Objects.equal(delays, other.delays) && Objects.equal(info, other.info);
        }
        return false;
    }

    @Override
    public String toString() {
        ToStringHelper<IndexReport> h = Metas.toStringHelper(this).add(INDEX_STARTED).add(INDEX_ACTIVE).add(INDEX_STATUS);
        if (level != IndexReportLevel.BASIC) {
            h.add("delays", delays.get());
            h.add("info", info.get());
        }
        return h.toString();
    }
}
