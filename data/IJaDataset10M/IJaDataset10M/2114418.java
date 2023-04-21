package com.google.gwt.gadgets.sample.mealpreferences.client;

import com.google.gwt.gadgets.client.BooleanPreference;
import com.google.gwt.gadgets.client.UserPreferences;

public interface MealPreferences extends UserPreferences {

    @PreferenceAttributes(display_name = "Vegetarian", default_value = "false")
    BooleanPreference noMeat();
}
