package dk.benches.domain;

import java.io.Serializable;

public class Bench implements Serializable {

    private Location location;

    private Rating rating;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Country: ").append(location.getCountry());
        builder.append("\nCity: ").append(location.getCity());
        builder.append("\nStreet: ").append(location.getStreet());
        builder.append("\nDescription: ").append(description);
        builder.append("\nLocation rating: ").append(rating.getLocationRating());
        builder.append("\nComfort rating: ").append(rating.getComfortRating());
        builder.append("\nFacilities rating: ").append(rating.getFacilitiesRating());
        return builder.toString();
    }
}
