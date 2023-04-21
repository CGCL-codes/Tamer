package com.jj.abtesting.process;

import com.jj.abtesting.exception.ABException;

/**
 * Interface for anyone who wants to generate a version based on the ratio
 * and for a particular user for whom the version is seeked. <p>this process
 * is not tied to any particular element for which the versions are
 * generated, it could be anything for example a style sheet include or a
 * jsp include.</p>
 * <p>
 * To understand what is A/B Testing
 * {@link "http://en.wikipedia.org/wiki/A/B_testing" A/B Testing}
 *
 * @author john.p
 *
 */
public interface ABProcess {

    /**
    * Generates the version based on the group of versions passed in the
    * ABTestInfo. The version generated depends on the below factors
    * <ul>
    * <li>the user for whom the version generation was initiated
    * <li>the group from which a version to be picked up
    * <li>the ratio based on which the version will be generated
    * </ul>
    *
    * <p>
    * <h3>Example:</h3><br/>
    * if the group name is "testgroup" and the versions with their
    * weightage which is provided as a ratio ("1:1:2") are shown below
    * <ul>
    * <li>version1 - 1
    * <li>version2 - 1
    * <li>version3 - 2
    * </ul>
    *
    * every time a version generation is initiated for a user, if the
    * version was already generated for this user for this group and for
    * this same ratio then this method call will be intercepted by a spring
    * proxy and the version value will be returned from cache.
    *<p>
    * hence, it is very important to have the hashCode() method to be
    * overriden by the ABTestInfo.
    * </p>
    *
    * if the version is not generated for this criteria then based on the
    * weightage given the appropriate version will be given for example
    * version3 will be generated more than version1 and version2
    *
    *
    * @param info about the version and the corresponding ratio.
    * @return the version generated from a group of versions based on the
    *         ratio supplied
    * @throws ABException if any error while generating the version.
    */
    String generateVersion(ABTestInfo info) throws ABException;
}
