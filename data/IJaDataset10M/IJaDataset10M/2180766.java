package net.sf.mailsomething.auth.impl;

import net.sf.mailsomething.auth.Challenge;

/**
 * 
 * I have placed this interface in the impl package coz it is part
 * of the implementation, and not directly defined in other interfaces.
 * It is referenced/used in implementations. 
 * 
 * 
 * @author Stig Tanggaard
 * @created 07-05-2003
 * 
 */
public interface ChallengeListener {

    public void challengeSuccess(Challenge source);

    public void challengeFailure(Challenge source);
}
