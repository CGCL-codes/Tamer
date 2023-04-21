package com.technosophos.rhizome.repository;

public class DocumentNotFoundException extends RepositoryAccessException {

    private static final long serialVersionUID = 1L;

    public DocumentNotFoundException() {
        super("Cannot find the requested document.");
    }

    public DocumentNotFoundException(String str) {
        super(str);
    }
}
