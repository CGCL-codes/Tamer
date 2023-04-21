package com.google.code.ar;

class ArEntryValidator {

    static void validate(ArEntry entry) {
        if (entry == null) {
            return;
        }
        if (entry.getFilename() == null || entry.getFilename().trim().length() == 0) {
            throw new IllegalArgumentException("entry filename should be specified");
        }
        if (entry.getFileMode() < 0) {
            throw new IllegalArgumentException("entry file mode should be positive. got: " + entry.getFileMode());
        }
        if (entry.getGroupId() < 0) {
            throw new IllegalArgumentException("entry group id should be positive. got: " + entry.getGroupId());
        }
        if (entry.getOwnerId() < 0) {
            throw new IllegalArgumentException("entry owner id should be positive. got: " + entry.getOwnerId());
        }
    }

    static void validateInMemoryEntry(ArEntry entry) {
        validate(entry);
        if (entry.getData() == null) {
            throw new IllegalArgumentException("entry data should be specified or have 0 lenght");
        }
    }
}
