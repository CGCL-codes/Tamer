package com.innovative.plugins;

import java.util.Set;

/**
 * Defines the basic structure of a PlugIn
 *
 * @author Dylon Edwards
 * @since 0.2
 */
public interface PlugIn extends Constants {

    /**
	 * Returns the name of the author of this PlugIn
	 *
	 * @return The name of the author of this PlugIn
	 */
    String getAuthor();

    /**
	 * Returns the description of this PlugIn
	 *
	 * @return The description of this PlugIn
	 */
    String getDesc();

    /**
	 * Returns the long description of this PlugIn
	 *
	 * @return The long description of this PlugIn
	 */
    String getLongDesc();

    /**
	 * Returns the Email address of the author of this PlugIn
	 *
	 * @return The Email address of the author of this Plugin
	 */
    String getEmail();

    /**
	 * Returns the home page for this PlugIn
	 *
	 * @return The home page for this PlugIn
	 */
    String getHomePage();

    /**
	 * Returns the name of this PlugIn
	 *
	 * @return The name of this PlugIn
	 */
    String getName();

    /**
	 * Returns the name of the organization sponsoring this PlugIn
	 *
	 * @return The name of the organization sponsoring this PlugIn
	 */
    String getOrganization();

    /**
	 * Returns the {@link Type} of this PlugIn
	 *
	 * @return The {@link Type} of this PlugIn
	 */
    Type getType();

    /**
	 * Returns the version of this PlugIn
	 *
	 * @return The version of this PlugIn
	 */
    String getVersion();

    /**
	 * Returns a set of {@link Dependency}s for this PlugIn
	 *
	 * @return A set of {@link Dependency}s for this PlugIn
	 */
    Set<Dependency> getDependencies();

    /**
	 * Returns a set of {@link Conflict}s for this PlugIn
	 *
	 * @return A set of {@link Conflict}s for this PlugIn
	 */
    Set<Conflict> getConflicts();

    /**
	 * Returns a set of {@link Provision}s which are provided by this PlugIn
	 *
	 * @return A set of {@link Provision}s which are provided by this PlugIn
	 */
    Set<Provision> getProvisions();

    /**
	 * Returns a set of {@link Replacement}s that this PlugIn replaces
	 * 
	 * @return A set of {@link Replacement}s that this PlugIn replaces
	 */
    Set<Replacement> getReplacements();

    /**
	 * Returns a set of {@link Suggestion}s that this PlugIn works well with
	 *
	 * @return A set of {@link Suggestion}s that this PlugIn works well with
	 */
    Set<Suggestion> getSuggestions();

    /**
	 * Returns a set of {@link SupportedOS operating systems} supported by this PlugIn
	 *
	 * @return A set of {@link SupportedOS operating systems} supported by this PlugIn
	 */
    Set<SupportedOS> getSupportedOS();

    /**
	 * Called before this PlugIn is installed
	 */
    void preInstall();

    /**
	 * Called when this PlugIn is installed
	 */
    void install();

    /**
	 * Called after this PlugIn is installed
	 */
    void postInstall();

    /** 
	 * Initializes the PlugIn
	 */
    void init();

    /**
	 * Loads the PlugIn into the application
	 */
    void load();

    /**
	 * Unloads the PlugIn from the application
	 */
    void unload();

    /**
	 * Called before this PlugIn is upgraded
	 */
    void preUpgrade();

    /**
	 * Called when this PlugIn is upgraded
	 */
    void upgrade();

    /**
	 * Called after this PlugIn is upgraded
	 */
    void postUpgrade();

    /**
	 * Called before this PlugIn is downgraded
	 */
    void preDowngrade();

    /**
	 * Called when this PlugIn is downgraded
	 */
    void downgrade();

    /**
	 * Called after this PlugIn is downgraded
	 */
    void postDowngrade();

    /**
	 * Called before this PlugIn is uninstalled
	 */
    void preUninstall();

    /**
	 * Clled when this PlugIn is uninstalled
	 */
    void uninstall();

    /**
	 * Called after this PlugIn is uninstalled
	 */
    void postUninstall();
}
