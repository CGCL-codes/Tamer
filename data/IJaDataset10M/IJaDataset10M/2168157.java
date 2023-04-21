package net.sfjinyan.common;

/**
 * The generic representation of a user, common to a client and the server
 * @author Bogdan Vovk
 */
public class User {

    /**
     * Username (handle)
     */
    protected String username;

    /**
     * Password
     */
    protected String password;

    /**
     * Elo rating (Glicko2)
     */
    protected int rating;

    private int ratingDeviation;

    private double volatility;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getRating() {
        return rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRatingDeviation(int ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    public int getRatingDeviation() {
        return ratingDeviation;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getVolatility() {
        return volatility;
    }
}
