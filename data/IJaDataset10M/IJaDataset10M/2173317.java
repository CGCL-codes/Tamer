package com.phloc.holiday;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.joda.time.LocalDate;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Contains a map of holidays, were the key is the date.
 * 
 * @author philip
 */
public final class HolidayMap {

    private final Map<LocalDate, ISingleHoliday> m_aMap = new LinkedHashMap<LocalDate, ISingleHoliday>();

    public HolidayMap() {
    }

    public boolean containsHolidayForDate(@Nullable final LocalDate aDate) {
        return m_aMap.containsKey(aDate);
    }

    @Nullable
    public ISingleHoliday getHolidayForDate(@Nullable final LocalDate aDate) {
        return m_aMap.get(aDate);
    }

    public void add(@Nonnull final LocalDate aDate, @Nonnull final ISingleHoliday aHoliday) {
        if (aDate == null) throw new NullPointerException("date");
        if (aHoliday == null) throw new NullPointerException("holiday");
        m_aMap.put(aDate, aHoliday);
    }

    public void addAll(@Nonnull final HolidayMap aSubHolidays) {
        if (aSubHolidays == null) throw new NullPointerException("subHolidays");
        m_aMap.putAll(aSubHolidays.m_aMap);
    }

    @Nonnull
    public EChange remove(@Nullable final LocalDate aDate) {
        return EChange.valueOf(m_aMap.remove(aDate) != null);
    }

    @Nonnull
    @ReturnsImmutableObject
    public Set<LocalDate> getAllDates() {
        return ContainerHelper.makeUnmodifiable(m_aMap.keySet());
    }

    @Nonnull
    @ReturnsImmutableObject
    public Collection<ISingleHoliday> getAllHolidays() {
        return ContainerHelper.makeUnmodifiable(m_aMap.values());
    }

    @Nonnull
    @ReturnsImmutableObject
    public Map<LocalDate, ISingleHoliday> getMap() {
        return ContainerHelper.makeUnmodifiable(m_aMap);
    }

    @Nonnegative
    public int size() {
        return m_aMap.size();
    }

    public boolean isEmpty() {
        return m_aMap.isEmpty();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof HolidayMap)) return false;
        final HolidayMap rhs = (HolidayMap) o;
        return m_aMap.equals(rhs.m_aMap);
    }

    @Override
    public int hashCode() {
        return new HashCodeGenerator(this).append(m_aMap).getHashCode();
    }

    @Override
    public String toString() {
        return new ToStringGenerator(this).append("map", m_aMap).toString();
    }
}
