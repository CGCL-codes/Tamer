package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.mapper;

import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.matcher.RbtEntityToOWLEntityMatcherFactory;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.matcher.RbtEntityToOWLEntityMatcherFactoryImpl;
import com.google.inject.Binder;
import com.google.inject.Module;

public class RbtToOwlEntityMapperModule implements Module {

    public void configure(Binder binder) {
        binder.bind(RbtToOwlApiEntityMapperFactory.class).to(RbtToOwlApiEntityMapperFactoryImpl.class);
        binder.bind(RbtEntityToOWLEntityMatcherFactory.class).to(RbtEntityToOWLEntityMatcherFactoryImpl.class);
    }
}
