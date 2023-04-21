package com.andrewswan.gamenight.domain;

/**
 * A persisted entity
 * 
 * @author Admin
 * @param <I> the type of ID it has
 */
public interface Entity<I> {

    I getId();
}
