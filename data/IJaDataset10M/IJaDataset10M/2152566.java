package ru.ifmo.rain.astrans.asttomodel.bootstrap.impl;

import ru.ifmo.rain.astrans.AstransFactory;
import ru.ifmo.rain.astrans.CreateClass;
import ru.ifmo.rain.astrans.CreatedEClass;

public class CreatedClasses extends SingleClassifierNamespace<CreatedEClass, CreateClass> {

    @Override
    protected CreatedEClass createReference(CreateClass value) {
        CreatedEClass result = AstransFactory.eINSTANCE.createCreatedEClass();
        result.setCreate(value);
        return result;
    }

    @Override
    protected String getName(CreateClass value) {
        return value.getName();
    }
}
