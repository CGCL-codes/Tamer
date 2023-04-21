package net.sourceforge.greenvine.reveng.impl;

import java.io.File;
import java.io.FileNotFoundException;
import net.sourceforge.greenvine.database.Database;
import net.sourceforge.greenvine.reveng.ReverseEngineer;
import net.sourceforge.greenvine.reveng.ReverseEngineerFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public class ReverseEngineerFactoryImpl implements ReverseEngineerFactory {

    public ReverseEngineer getInstance(Database database, File configFile, File outputFile) throws MarshalException, ValidationException, FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return new ReverseEngineerImpl(database, configFile, outputFile);
    }
}
