package com.jemframework.domain.identifier;

import com.jemframework.domain.exception.IdentifierConverterException;

public interface IdentifierFactory {

    Identifier createIdentifier(String identifierAsString) throws IdentifierConverterException;

    String getIdentifierAsString(Identifier entityId);
}
