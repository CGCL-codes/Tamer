package dex.compiler.problem;

import dex.compiler.model.base.Place;

/**
 * Indicates that something illegal was attempted.
 */
public final class Crime extends Problem {

    /**
	 * Constructs a new crime.
	 * 
	 * @param msg  the message describing the crime
	 */
    public Crime(String msg) {
        super(msg);
    }

    public String getMessage(Place place, String construct) {
        return place + ": " + this + construct;
    }
}
