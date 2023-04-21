package com.farata.cleardatabuilder.migration;

import org.eclipse.core.runtime.IPath;

public class MigrationRecord {

    public enum MigrationObject {

        SINGLE_FILE, ALL_FILES_RECURSIVELY, ALL_FILES_IN_FOLDER;

        public boolean isFolder() {
            return this != SINGLE_FILE;
        }

        public boolean isFile() {
            return this == SINGLE_FILE;
        }

        public boolean isRecursively() {
            return this == ALL_FILES_RECURSIVELY;
        }
    }

    public enum MigrationMethod {

        OVERRIDE, OVERRIDE_REMOVE;

        public boolean isRemoving() {
            return this == OVERRIDE_REMOVE;
        }
    }

    public IPath source;

    public IPath target;

    public MigrationMethod method;

    public MigrationObject object;
}
