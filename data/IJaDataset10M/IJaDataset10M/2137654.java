package com.google.code.ssm.test.dao;

import java.util.*;

/**
 * 
 * @author Nelson Carpentier
 * 
 */
public interface TestDAO {

    public String getDateString(final String key);

    public void overrideDateString(final int trash, final String key, final String overrideData);

    public List<String> getTimestampValues(final List<Long> keys);

    public String updateTimestampValue(final Long key);

    public List<String> updateTimestamValues(final List<Long> keys);

    public void overrideTimestampValues(final int trash, final List<Long> keys, final String nuthin, final List<String> overrideData);

    public String getRandomString(final Long key);

    public void updateRandomString(final Long key);

    public Long updateRandomStringAgain(final Long key);

    public List<String> getRandomStrings(final List<Long> keys);

    public void updateRandomStrings(final List<Long> keys);

    public List<Long> updateRandomStringsAgain(final List<Long> keys);

    public List<String> getAssignStrings();

    public void invalidateAssignStrings();

    public void updateAssignStrings(int bubpkus, final List<String> newData);

    public void increment(String key);

    public void decrement(String key);

    public long getCounter(String key);

    public void update(String key, Long value);

    public void invalidate(String key);
}
