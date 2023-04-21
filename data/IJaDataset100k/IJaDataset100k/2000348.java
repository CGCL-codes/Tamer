package org.colllib.transformer;

/**
 * A transformer interface: put one object in, get another object out.
 * 
 * This file is part of CollLib released under the terms of the LGPL V3.0.
 * See the file licenses/lgpl-3.0.txt for details.
 * 
 * @author marc.jackisch
 */
public interface Transformer<S, D> {

    D transform(S s);
}
