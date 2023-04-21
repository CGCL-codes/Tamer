package com.miladinovicmarko.boxingworld.model;

public interface Tournament {

    public BoxingArena getBoxingArena();

    public void setBoxingArena(BoxingArena boxingArena);

    public String getDescription();

    public void setDescription(String description);

    public Long get_id();

    public String toString();

    public SignInForTournament getSignInForTournament();

    public void setSignInForTournament(SignInForTournament signInForTournament);

    public boolean isSignedIn();

    public void setSignedIn(boolean signedIn);
}
