package edu.osu.cse.be.model.bd.i;

import edu.osu.cse.be.model.Referee;
import edu.osu.cse.be.model.exceptions.InvalidRefereeException;

public interface RefereeManagementDelegate {

    public void createNewReferee(Referee ref) throws InvalidRefereeException;

    public void updateReferee(Referee ref) throws InvalidRefereeException;

    public Referee authenticate(String userName, String password) throws InvalidRefereeException;

    public Referee findReferee(String userName) throws InvalidRefereeException;
}
