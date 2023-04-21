package org.hmaciel.rph.consultants.finders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.comunication.helper.constats.ClassCodes;
import org.hmaciel.rph.constants.CodesSystemNames;
import org.hmaciel.rph.constants.CodesSystems;
import org.hmaciel.rph.constants.TypeCodes;
import org.hmaciel.rph.ejb.entity.NationClass;
import org.hmaciel.rph.ejb.entity.OrganizationClass;
import org.hmaciel.rph.ejb.rols.CitizenClass;
import org.hmaciel.rph.ejb.rols.EmploymentClass;
import org.hmaciel.rph.ejb.rols.OtherIdsClass;
import org.hmaciel.rph.ejb.rols.PatientRolClass;
import org.hmaciel.rph.ejb.rols.PersonalRelationShipClass;
import org.hmaciel.rph.ejb.rols.RoleClass;
import org.hmaciel.rph.ejb.utils.ADClass;
import org.hmaciel.rph.ejb.utils.ADXPClass;
import org.hmaciel.rph.ejb.utils.CEClass;
import org.hmaciel.rph.ejb.utils.CSClass;
import org.hmaciel.rph.ejb.utils.ENClass;
import org.hmaciel.rph.ejb.utils.ENXPClass;
import org.hmaciel.rph.ejb.utils.IIClass;
import org.hmaciel.rph.ejb.utils.TELClass;
import org.hmaciel.rph.ejb.utils.TSClass;
import org.hmaciel.rph.rimclassextension.PersonClassExtension;
import org.opensih.servicioJMX.invocador.InvocadorService;
import org.opensih.servicioJMX.invocador.InvocadorServiceBean;
import com.novell.ldap.LDAPSearchResults;

public class MakePersonClass {

    public static PersonClassExtension makePersonFromEntries(LDAPSearchResults searchResults, String dn) {
        Map<String, Tupla> mapperDns = Mappers.mapDNWithTuplas(searchResults);
        Collection<Tupla> tuplas = mapperDns.values();
        Tupla personTupla = Mappers.getPersonFromTuplas(tuplas);
        PersonClassExtension person = null;
        if (personTupla != null) person = (PersonClassExtension) personTupla.getElement();
        List<IIClass> l_ii = new ArrayList<IIClass>();
        List<IIClass> orgl_ii = new ArrayList<IIClass>();
        List<ENXPClass> enxpList = new LinkedList<ENXPClass>();
        List<ENXPClass> orgEnxpList = new LinkedList<ENXPClass>();
        List<ADXPClass> adxpslist = new LinkedList<ADXPClass>();
        List<RoleClass> rols = new LinkedList<RoleClass>();
        List<TELClass> tels = new ArrayList<TELClass>();
        List<CEClass> raceList = new ArrayList<CEClass>();
        PatientRolClass patient = new PatientRolClass();
        OrganizationClass org = null;
        CSClass cs = new CSClass();
        cs.setCode(ClassCodes.PATIENT);
        patient.setClassCode(cs);
        patient.setStatusCode(new CSClass());
        person.setBirthTime(new TSClass());
        person.setAdministrativeGenderCode(new CEClass());
        for (Iterator<Tupla> iter = tuplas.iterator(); iter.hasNext(); ) {
            Tupla element = (Tupla) iter.next();
            if (element.mydn.equals("type=ENXP_0,type=EN_0,type=ORG," + dn)) orgEnxpList.add((ENXPClass) element.getElement()); else if (element.type.equals(TypeCodes.NAME)) enxpList.add((ENXPClass) element.getElement()); else if (element.type.equals(TypeCodes.CODE_E)) {
                CEClass ce = (CEClass) element.getElement();
                if (ce.getCodeSystem().equals(CodesSystems.RACE_CODE_PERSON)) raceList.add(ce); else {
                    ce.setCodeSystem(CodesSystems.ADMINISTRATIVE_GENDER_CODE_PERSON);
                    person.setAdministrativeGenderCode(ce);
                }
            } else if (element.type.equals(TypeCodes.ADDRESS)) adxpslist.add((ADXPClass) element.getElement()); else if (element.mydn.equals("type=II_0,type=ORG," + dn)) orgl_ii.add((IIClass) element.getElement()); else if (element.type.equals(TypeCodes.IDENTIFIER)) l_ii.add((IIClass) element.getElement()); else if (element.mydn.equals("type=CS_1," + dn)) patient.setStatusCode((CSClass) element.getElement()); else if (element.type.equals(TypeCodes.TIME)) person.setBirthTime((TSClass) element.getElement()); else if (element.type.equals(TypeCodes.TELEPHONE)) tels.add((TELClass) element.getElement()); else if (element.type.equals(TypeCodes.ORGANIZATION)) org = (OrganizationClass) element.getElement();
        }
        if (org != null) {
            org.setId(orgl_ii);
            ENClass ec2 = new ENClass();
            ec2.setNombres(orgEnxpList);
            List<ENClass> len2 = new ArrayList<ENClass>();
            len2.add(ec2);
            org.setName(len2);
            patient.setScoper(org);
        }
        patient.setId(l_ii);
        rols.add(patient);
        person.setPlayedRole(rols);
        ENClass ec = new ENClass();
        ec.setNombres(enxpList);
        List<ENClass> len = new ArrayList<ENClass>();
        len.add(ec);
        person.setName(len);
        ADClass d = new ADClass();
        d.setAddr(adxpslist);
        List<ADClass> l_ad = new ArrayList<ADClass>();
        l_ad.add(d);
        person.setAddr(l_ad);
        person.setTelecom(tels);
        person.setRaceCode(raceList);
        return person;
    }

    /**
	 * @version not use
	 * @deprecated
	 * @param mapperDns
	 * @return
	 */
    public PersonClassExtension makePersonFromEntries2(LDAPSearchResults searchResults, String extension) {
        Map<String, Tupla> mapperDns = Mappers.mapDNWithTuplas(searchResults);
        Collection<Tupla> tuplas = mapperDns.values();
        Tupla personTupla = Mappers.getPersonFromTuplas(tuplas);
        PersonClassExtension person = null;
        if (personTupla != null) person = (PersonClassExtension) personTupla.getElement();
        PatientRolClass patient = null;
        List<IIClass> l_ii = new ArrayList<IIClass>();
        List<ENXPClass> enxpList = new LinkedList<ENXPClass>();
        List<ADXPClass> adxpslist = new LinkedList<ADXPClass>();
        String tipoApellido = null;
        String nombre = null;
        String tipoNombre = null;
        String apellido = null;
        String genderCode = null;
        String direccion = null;
        String tipoDir = null;
        String direccion2 = null;
        String tipoDir2 = null;
        String direccion3 = null;
        String tipoDir3 = null;
        String direccion4 = null;
        String tipoDir4 = null;
        String root = null;
        String extention = null;
        String mostrable = null;
        String codigoActivo = null;
        String codeSystemActive = null;
        String telefono = null;
        Date dataTs = null;
        for (Iterator<Tupla> iter = tuplas.iterator(); iter.hasNext(); ) {
            Tupla element = (Tupla) iter.next();
            if (element.mydn.replaceAll(" ", "").equals("type=ENXP_0,type=EN_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                apellido = ((ENXPClass) element.getElement()).getNombre();
                tipoApellido = ((ENXPClass) element.getElement()).getTipo();
            } else {
                if (element.mydn.replaceAll(" ", "").equals("type=CS_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                } else {
                    if (element.mydn.replaceAll(" ", "").equals("type=CE_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                        genderCode = ((CEClass) element.getElement()).getCode();
                    } else {
                        if (element.mydn.replaceAll(" ", "").equals("type=ADXP_0,type=AD_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                            direccion = ((ADXPClass) element.getElement()).getDir();
                            tipoDir = ((ADXPClass) element.getElement()).getTipo();
                        } else {
                            if (element.mydn.replaceAll(" ", "").equals("type=ADXP_1,type=AD_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                                direccion2 = ((ADXPClass) element.getElement()).getDir();
                                tipoDir2 = ((ADXPClass) element.getElement()).getTipo();
                            } else {
                                if (element.mydn.replaceAll(" ", "").equals("type=ADXP_2,type=AD_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                                    direccion3 = ((ADXPClass) element.getElement()).getDir();
                                    tipoDir3 = ((ADXPClass) element.getElement()).getTipo();
                                }
                                if (element.mydn.replaceAll(" ", "").equals("type=ADXP_3,type=AD_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                                    direccion4 = ((ADXPClass) element.getElement()).getDir();
                                    tipoDir4 = ((ADXPClass) element.getElement()).getTipo();
                                } else {
                                    if (element.mydn.replaceAll(" ", "").equals("type=II_0,type=PAT_" + extension + ",dc=hmaciel")) {
                                        root = ((IIClass) element.getElement()).getATTR_ROOT();
                                        extention = ((IIClass) element.getElement()).getATTR_EXTENSION();
                                        mostrable = ((IIClass) element.getElement()).getATTR_DISPLAYABLE();
                                    } else {
                                        if (element.mydn.replaceAll(" ", "").equals("type=AD_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                                        } else {
                                            if (element.mydn.replaceAll(" ", "").equals("type=CS_1, type=PAT_" + extension + ",dc=hmaciel")) {
                                                codigoActivo = ((CSClass) element.getElement()).getCode();
                                                codeSystemActive = ((CSClass) element.getElement()).getCodeSystem();
                                            } else {
                                                if (element.mydn.replaceAll(" ", "").equals("type=ENXP_1,type=EN_0,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                                                    nombre = ((ENXPClass) element.getElement()).getNombre();
                                                    tipoNombre = ((ENXPClass) element.getElement()).getTipo();
                                                } else {
                                                    if (element.mydn.replaceAll(" ", "").equals("type=TS,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) dataTs = ((TSClass) element.getElement()).getDate(); else {
                                                        if (element.mydn.replaceAll(" ", "").equals("type=TEL,type=PSN,type=PAT_" + extension + ",dc=hmaciel")) {
                                                            telefono = ((TELClass) element.getElement()).getTel();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        List<RoleClass> rols = new LinkedList<RoleClass>();
        IIClass ii1 = new IIClass();
        ii1.setATTR_ROOT(root);
        ii1.setATTR_EXTENSION(extention);
        ii1.setATTR_DISPLAYABLE(mostrable);
        l_ii.add(ii1);
        patient = new PatientRolClass();
        CSClass cs = new CSClass();
        cs.setCode(ClassCodes.PATIENT);
        patient.setClassCode(cs);
        patient.setId(l_ii);
        CSClass csActive = new CSClass();
        csActive.setCode(codigoActivo);
        csActive.setCodeSystem(codeSystemActive);
        patient.setStatusCode(csActive);
        rols.add(patient);
        person.setPlayedRole(rols);
        ENXPClass EXP1 = new ENXPClass();
        EXP1.setNombre(apellido);
        EXP1.setTipo(tipoApellido);
        enxpList.add(EXP1);
        ENXPClass EXP2 = new ENXPClass();
        EXP2.setNombre(nombre);
        EXP2.setTipo(tipoNombre);
        enxpList.add(EXP2);
        ENClass ec = new ENClass();
        ec.setNombres(enxpList);
        List<ENClass> len = new ArrayList<ENClass>();
        len.add(ec);
        person.setName(len);
        ADXPClass ADXP0 = new ADXPClass();
        ADXP0.setDir(direccion);
        ADXP0.setTipo(tipoDir);
        adxpslist.add(ADXP0);
        ADXPClass ADXP1 = new ADXPClass();
        ADXP1.setDir(direccion2);
        ADXP1.setTipo(tipoDir2);
        adxpslist.add(ADXP1);
        ADXPClass ADXP2 = new ADXPClass();
        ADXP2.setDir(direccion3);
        ADXP2.setTipo(tipoDir3);
        adxpslist.add(ADXP2);
        ADXPClass ADXP3 = new ADXPClass();
        ADXP3.setDir(direccion4);
        ADXP3.setTipo(tipoDir4);
        adxpslist.add(ADXP3);
        ADClass d = new ADClass();
        d.setAddr(adxpslist);
        List<ADClass> l_ad = new ArrayList<ADClass>();
        l_ad.add(d);
        person.setAddr(l_ad);
        if (telefono != null) {
            TELClass phone = new TELClass();
            phone.setTel(telefono);
            List<TELClass> tels = new ArrayList<TELClass>();
            tels.add(phone);
            person.setTelecom(tels);
        }
        CEClass administrativeGenderCode = new CEClass();
        administrativeGenderCode.setCode(genderCode);
        administrativeGenderCode.setCodeSystem(CodesSystems.ADMINISTRATIVE_GENDER_CODE_PERSON);
        person.setAdministrativeGenderCode(administrativeGenderCode);
        TSClass effectiveTime = new TSClass();
        effectiveTime.setDate(dataTs);
        person.setBirthTime(effectiveTime);
        List<ENClass> l = person.getName();
        Iterator<ENClass> it = l.iterator();
        while (it.hasNext()) {
            ENClass en = (ENClass) it.next();
            List<ENXPClass> enxp = en.getNombres();
            Iterator<ENXPClass> itxp = enxp.iterator();
            while (itxp.hasNext()) {
                itxp.next();
            }
        }
        return person;
    }

    /**
	 * @author pclavijo
	 * 
	 * @deprecated
	 * @param mapperDns
	 * @version Not used (compara preguntando por los CodesSystemNames de los
	 *          elementos)
	 * @return
	 */
    public PersonClassExtension makePersonFromEntriesByCodesSystemNamesCompatation(LDAPSearchResults searchResults) {
        Map<String, Tupla> mapperDns = Mappers.mapDNWithTuplas(searchResults);
        Collection<Tupla> tuplas = mapperDns.values();
        Tupla personTupla = Mappers.getPersonFromTuplas(tuplas);
        PersonClassExtension person = (PersonClassExtension) personTupla.getElement();
        Map<String, CitizenClass> citizensPerson = new HashMap<String, CitizenClass>();
        Map<String, EmploymentClass> employmentPerson = new HashMap<String, EmploymentClass>();
        Map<String, PersonalRelationShipClass> personalRelationShipPerson = new HashMap<String, PersonalRelationShipClass>();
        Map<String, OtherIdsClass> otherIdsPerson = new HashMap<String, OtherIdsClass>();
        PatientRolClass patient = null;
        InvocadorService inv = InvocadorServiceBean.getInstance();
        for (Iterator<Tupla> iter = tuplas.iterator(); iter.hasNext(); ) {
            Tupla element = (Tupla) iter.next();
            if (!element.getFatherDn().equalsIgnoreCase(inv.getDnBase())) {
                Tupla padre = mapperDns.get(element.getFatherDn());
                if (element.getType().equalsIgnoreCase(TypeCodes.NAME)) {
                    ((ENClass) padre.getElement()).getNombres().add((ENXPClass) element.getElement());
                } else if (element.getType().equalsIgnoreCase(TypeCodes.COMPLETE_ADDRESS)) {
                    if (padre != null) {
                        if (padre.getType().equalsIgnoreCase(TypeCodes.PERSON)) {
                            ((PersonClassExtension) mapperDns.get(padre.getMydn()).getElement()).getAddr().add((ADClass) element.getElement());
                        }
                    }
                } else if (element.getType().equalsIgnoreCase(TypeCodes.ADDRESS)) {
                    ((ADClass) padre.getElement()).getAddr().add((ADXPClass) element.getElement());
                } else if (element.getType().equalsIgnoreCase(TypeCodes.COMPLETE_NAME)) {
                    if (padre != null) {
                        if (padre.getType().equalsIgnoreCase(TypeCodes.PERSON)) {
                            ((PersonClassExtension) mapperDns.get(padre.getMydn()).getElement()).getName().add((ENClass) element.getElement());
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.NATION)) {
                            ((NationClass) mapperDns.get(padre.getMydn()).getElement()).getName().add((ENClass) element.getElement());
                        }
                    }
                } else if (element.getType().equalsIgnoreCase(TypeCodes.IDENTIFIER)) {
                    if (padre != null) {
                        if (padre.getType().equalsIgnoreCase(TypeCodes.CITIZEN)) {
                            ((CitizenClass) mapperDns.get(padre.getMydn()).getElement()).getId().add((IIClass) element.getElement());
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.EMPLOYMENT)) {
                            ((EmploymentClass) mapperDns.get(padre.getMydn()).getElement()).getId().add((IIClass) element.getElement());
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.PERSONAL_RELATION_SHIP)) {
                            ((EmploymentClass) mapperDns.get(padre.getMydn()).getElement()).getId().add((IIClass) element.getElement());
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.OTHER_ID)) {
                            ((OtherIdsClass) mapperDns.get(padre.getMydn()).getElement()).getId().add((IIClass) element.getElement());
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.PATIENT)) {
                            if (patient != null) {
                                patient.getId().add((IIClass) element.getElement());
                            } else {
                                ((PatientRolClass) mapperDns.get(padre.getMydn()).getElement()).getId().add((IIClass) element.getElement());
                            }
                        }
                    }
                } else if (element.getType().equalsIgnoreCase(TypeCodes.CITIZEN)) {
                    citizensPerson.put(element.getMydn(), (CitizenClass) element.getElement());
                } else if (element.getType().equalsIgnoreCase(TypeCodes.EMPLOYMENT)) {
                    employmentPerson.put(element.getMydn(), (EmploymentClass) element.getElement());
                } else if (element.getType().equalsIgnoreCase(TypeCodes.PERSONAL_RELATION_SHIP)) {
                    personalRelationShipPerson.put(element.getMydn(), (PersonalRelationShipClass) element.getElement());
                } else if (element.getType().equalsIgnoreCase(TypeCodes.OTHER_ID)) {
                    otherIdsPerson.put(element.getMydn(), (OtherIdsClass) element.getElement());
                } else if (element.getType().equalsIgnoreCase(TypeCodes.PATIENT)) {
                    patient = (PatientRolClass) element.getElement();
                } else if (element.getType().equalsIgnoreCase(TypeCodes.NATION)) {
                    NationClass nation = (NationClass) element.getElement();
                    if (padre != null) {
                        ((CitizenClass) mapperDns.get(padre.getMydn()).getElement()).setScoper(nation);
                    }
                } else if (element.getType().equalsIgnoreCase(TypeCodes.CODE)) {
                    CSClass csElement = (CSClass) element.getElement();
                    if (padre != null) {
                        if (padre.getType().equalsIgnoreCase(TypeCodes.CITIZEN)) {
                            if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.CLASS_CODE)) {
                                ((CitizenClass) mapperDns.get(padre.getMydn()).getElement()).setClassCode(csElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.PERSON)) {
                            if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.DETERMINER_CODE)) {
                                ((PersonClassExtension) mapperDns.get(padre.getMydn()).getElement()).setDeterminerCode(csElement);
                            } else if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.CLASS_CODE)) {
                                ((PersonClassExtension) mapperDns.get(padre.getMydn()).getElement()).setClassCode(csElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.NATION)) {
                            if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.DETERMINER_CODE)) {
                                ((NationClass) mapperDns.get(padre.getMydn()).getElement()).setDeterminerCode(csElement);
                            } else if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.CLASS_CODE)) {
                                ((NationClass) mapperDns.get(padre.getMydn()).getElement()).setClassCode(csElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.OTHER_ID)) {
                            if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.CLASS_CODE)) {
                                ((OtherIdsClass) mapperDns.get(padre.getMydn()).getElement()).setClassCode(csElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.PATIENT)) {
                            if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.CLASS_CODE)) {
                                if (patient == null) {
                                    patient = ((PatientRolClass) mapperDns.get(padre.getMydn()).getElement());
                                }
                                patient.setClassCode(csElement);
                            } else if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.STAUS_CODE)) {
                                if (patient == null) {
                                    patient = ((PatientRolClass) mapperDns.get(padre.getMydn()).getElement());
                                }
                                patient.setStatusCode(csElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.EMPLOYMENT)) {
                            if (csElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.CLASS_CODE)) {
                                ((EmploymentClass) mapperDns.get(padre.getMydn()).getElement()).setClassCode(csElement);
                            }
                        }
                    }
                } else if (element.getType().equalsIgnoreCase(TypeCodes.CODE_E)) {
                    CEClass ceElement = (CEClass) element.getElement();
                    if (padre != null) {
                        if (padre.getType().equalsIgnoreCase(TypeCodes.PERSON)) {
                            if (ceElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.ADMINISTRATIVE_GENDER_CODE)) {
                                ((PersonClassExtension) mapperDns.get(padre.getMydn()).getElement()).setAdministrativeGenderCode(ceElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.PATIENT)) {
                            if (ceElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.VERY_IMPORTANT_PERSON_CODE)) {
                                if (patient == null) {
                                    patient = ((PatientRolClass) mapperDns.get(padre.getMydn()).getElement());
                                }
                                patient.setVeryImportantPersonCode(ceElement);
                            } else if (ceElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.PATIENT_CODE)) {
                                if (patient == null) {
                                    patient = ((PatientRolClass) mapperDns.get(padre.getMydn()).getElement());
                                }
                                patient.setCode(ceElement);
                            }
                        } else if (padre.getType().equalsIgnoreCase(TypeCodes.NATION)) {
                            if (ceElement.getCodeSystemName().equalsIgnoreCase(CodesSystemNames.NATION_CODE)) {
                                ((NationClass) mapperDns.get(padre.getMydn()).getElement()).setCode(ceElement);
                            }
                        }
                    }
                }
            }
        }
        List<RoleClass> rols = new LinkedList<RoleClass>();
        rols.addAll(citizensPerson.values());
        rols.addAll(employmentPerson.values());
        rols.addAll(personalRelationShipPerson.values());
        rols.addAll(otherIdsPerson.values());
        rols.add(patient);
        person.setScopedRole(rols);
        return person;
    }
}
