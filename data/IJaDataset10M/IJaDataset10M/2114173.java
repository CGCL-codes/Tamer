package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbConnection.IDataBaseConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.IDataBaseApiUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.IErrorManagement;

public interface IDataBaseApiTools extends IErrorManagement, IDataBaseApiUtilities, IDataBaseConnection {
}
