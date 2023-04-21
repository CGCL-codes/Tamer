package org.tokaf;

import org.tokaf.datasearcher.DataSearcher;
import org.tokaf.normalizer.Normalizer;

/**
 * <p> TopKElement represent object in top-k algorithm. Contains M atributes and
 * name. </p> <p> Copyright (c) 2006 </p> <p>
 * @author Alan Eckhardt
 * @version 1.0
 */
public class TopKElement {

    public Object name;

    public double rating;

    Object ratingsObjects[];

    double ratings[];

    long foundAt[];

    long positionAt[];

    public TopKElement(Object name, Object[] ratings, DataSearcher[] data) {
        this.ratingsObjects = ratings;
        this.ratings = new double[ratingsObjects.length];
        for (int i = 0; i < ratingsObjects.length; i++) setRating(i, ratings[i], data[i]);
        this.name = name;
    }

    public TopKElement(Object name, Object[] ratings) {
        this.ratingsObjects = ratings;
        this.ratings = new double[ratingsObjects.length];
        this.name = name;
    }

    public TopKElement(Object name, double rating, int ratingsCount) {
        this.rating = rating;
        this.name = name;
        ratingsObjects = new Object[ratingsCount];
        this.ratings = new double[ratingsCount];
        for (int i = 0; i < ratingsCount; i++) {
            this.ratingsObjects[i] = null;
            this.ratings[i] = 0;
        }
    }

    public TopKElement(Object name, double rating) {
        this.rating = rating;
        this.name = name;
    }

    public TopKElement(Object name, int ratingsCount) {
        this.rating = -1;
        this.name = name;
        ratingsObjects = new Object[ratingsCount];
        this.ratings = new double[ratingsCount];
        for (int i = 0; i < ratingsCount; i++) {
            this.ratingsObjects[i] = null;
            this.ratings[i] = 0;
        }
    }

    public TopKElement(Object name) {
        this.rating = -1;
        this.name = name;
    }

    public int getLength() {
        if (ratings == null) return -1;
        return ratings.length;
    }

    public boolean isNull(int index) {
        return ratingsObjects[index] == null;
    }

    public Object getRatingObject(int index) {
        return ratingsObjects[index];
    }

    public double getRating(int index) {
        return ratings[index];
    }

    public double[] getRatings(int index) {
        return ratings;
    }

    public Object[] getRatingObjects() {
        return ratingsObjects;
    }

    public void setRating(int index, Object o, DataSearcher data) {
        setRating(index, o, data.getNormalizer());
    }

    public void setRating(int index, Object o, Normalizer norm) {
        ratingsObjects[index] = o;
        ratings[index] = norm.Normalize(o);
    }

    public void setRatingObject(int index, Object o) {
        ratingsObjects[index] = o;
    }

    public void setPosition(int index, int row) {
        if (positionAt == null) {
            positionAt = new long[ratings.length];
            for (int i = 0; i < positionAt.length; i++) positionAt[i] = -1;
        }
        positionAt[index] = row;
    }

    public long getPosition(int index) {
        return positionAt[index];
    }

    public void setFound(int index, long row) {
        if (foundAt == null) {
            foundAt = new long[ratings.length];
            for (int i = 0; i < foundAt.length; i++) foundAt[i] = -1;
        }
        foundAt[index] = row;
    }

    public long getFound(int index) {
        return foundAt[index];
    }
}
