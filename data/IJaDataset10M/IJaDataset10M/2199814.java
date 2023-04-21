package org.admin.controllers;

import org.perla.services.NoRelationException_Exception;
import org.perla.services.NoSuchSensorException_Exception;
import org.perla.services.Sensori;
import org.perla.services.WrongSensorTypeException_Exception;

/**
 * Classe statica che gestisce la comunicazione con il servizio di amministrazione
 * del PerlaController.
 *
 * @author Ricardo Gonzalez
 * @author Eva Gjeci
 */
public class PerlaController {

    public static java.util.List<org.perla.services.Sensori> getSensors() {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getSensors();
    }

    public static java.util.List<org.perla.services.Nodo> getNodes() {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getNodes();
    }

    public static java.util.List<org.perla.services.Temperature> getTemperatureValues(java.lang.String idNodo, java.lang.String idSensore, int limit) throws WrongSensorTypeException_Exception, NoSuchSensorException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getTemperatureValues(idNodo, idSensore, limit);
    }

    public static Sensori getSensor(java.lang.String idNodo, java.lang.String idSensore) throws NoSuchSensorException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getSensor(idNodo, idSensore);
    }

    public static java.util.List<org.perla.services.Attitude> getAttitudeValues(java.lang.String idNodo, java.lang.String idSensore, int limit) throws NoSuchSensorException_Exception, WrongSensorTypeException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getAttitudeValues(idNodo, idSensore, limit);
    }

    public static java.util.List<org.perla.services.Bibi> getBibiValues(java.lang.String idNodo, java.lang.String idSensore, int limit) throws WrongSensorTypeException_Exception, NoSuchSensorException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getBibiValues(idNodo, idSensore, limit);
    }

    public static java.util.List<org.perla.services.Crackmeter> getCrackmeterValues(java.lang.String idNodo, java.lang.String idSensore, int limit) throws NoSuchSensorException_Exception, WrongSensorTypeException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getCrackmeterValues(idNodo, idSensore, limit);
    }

    public static java.util.List<org.perla.services.Sensori> getSensorsByNode(java.lang.String idNodo) {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getSensorsByNode(idNodo);
    }

    public static java.util.List<org.perla.services.Relation> getRelations() {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getRelations();
    }

    public static java.util.List<org.perla.services.Attitude> getAttitudeValuesByDate(java.lang.String idNodo, java.lang.String idSensore, long from, long to) throws WrongSensorTypeException_Exception, NoSuchSensorException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getAttitudeValuesByDate(idNodo, idSensore, from, to);
    }

    public static boolean addRelation(java.lang.String idNodo, java.lang.String nome, boolean notify) {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.addRelation(idNodo, nome, notify);
    }

    public static java.util.List<org.perla.services.Crackmeter> getCrackmeterValuesByDate(java.lang.String idNodo, java.lang.String idSensore, long from, long to) throws NoSuchSensorException_Exception, WrongSensorTypeException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getCrackmeterValuesByDate(idNodo, idSensore, from, to);
    }

    public static java.util.List<org.perla.services.Temperature> getTemperatureValuesByDate(java.lang.String idNodo, java.lang.String idSensore, long from, long to) throws WrongSensorTypeException_Exception, NoSuchSensorException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getTemperatureValuesByDate(idNodo, idSensore, from, to);
    }

    public static java.util.List<org.perla.services.Bibi> getBibiValuesByDate(java.lang.String idNodo, java.lang.String idSensore, long from, long to) throws NoSuchSensorException_Exception, WrongSensorTypeException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.getBibiValuesByDate(idNodo, idSensore, from, to);
    }

    public static boolean updateAll() {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.updateAll();
    }

    public static void removeRelation(java.lang.String idGis) throws NoRelationException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        port.removeRelation(idGis);
    }

    public static boolean changeNotify(java.lang.String idGis) throws NoRelationException_Exception {
        org.perla.services.AdministrationService service = new org.perla.services.AdministrationService();
        org.perla.services.Administration port = service.getAdministrationPort();
        return port.changeNotify(idGis);
    }
}
