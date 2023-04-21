package com.ivis.xprocess.core.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.ivis.xprocess.core.AvailabilityRecord;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.Party;
import com.ivis.xprocess.core.PartyAvailability;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.core.Pricing;
import com.ivis.xprocess.core.Team;
import com.ivis.xprocess.framework.ContainerContainer;
import com.ivis.xprocess.framework.XchangeElementContainer;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.impl.XchangeElementContainerImpl;
import com.ivis.xprocess.framework.impl.XelementImpl;
import com.ivis.xprocess.framework.properties.PropertyDefinition;
import com.ivis.xprocess.framework.schema.SchemaRepository;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.util.Day;

public class OrganizationImpl extends XchangeElementContainerImpl implements Organization, ContainerContainer {

    private PartyImpl party;

    public OrganizationImpl(IPersistenceHelper ph) {
        super(ph);
        party = new PartyImpl(this);
    }

    public void buildLinks() {
        super.buildLinks();
        party.buildLinks();
    }

    public void unbuildLinks() {
        super.unbuildLinks();
        party.unbuildLinks();
    }

    @Override
    public List<Xelement> getExplorerContents() {
        List<Xelement> explorerContents = new ArrayList<Xelement>();
        explorerContents.addAll(getOrganizations());
        explorerContents.addAll(getPersons());
        return explorerContents;
    }

    public Organization createOrganization(String name) {
        Organization organization = super.getPersistenceHelper().createElement(Organization.class, this);
        organization.setAvailability(Day.ALWAYS, Day.NEVER, -1, -1, -1, -1, -1, -1, -1, this);
        organization.getPricing().setPriceRecordWithDefault(Day.ALWAYS, Day.NEVER, this);
        organization.setName(name);
        ((OrganizationImpl) organization).buildLinks();
        return organization;
    }

    public Person createPerson(String accountName) {
        Person person = super.getPersistenceHelper().createElement(Person.class, this);
        person.setAccountName(accountName);
        person.setAvailability(Day.ALWAYS, Day.NEVER, -1, -1, -1, -1, -1, -1, -1, this);
        person.getPricing().setPriceRecordWithDefault(Day.ALWAYS, Day.NEVER, this);
        person.createRole(PortfolioImpl.getParticipantRoletype(getPersistenceHelper()));
        ((PersonImpl) person).buildLinks();
        return person;
    }

    public Set<Person> getPersons() {
        return getContentsByType(Person.class);
    }

    public Set<Organization> getOrganizations() {
        return getContentsByType(Organization.class);
    }

    public Set<Organization> getAllOrganizations() {
        Set<Organization> organizations = new HashSet<Organization>();
        if (getOrganizations().size() > 0) {
            Set<Organization> children = this.getOrganizations();
            for (Organization childOrganization : children) {
                organizations.add(childOrganization);
                if (!childOrganization.getOrganizations().isEmpty()) {
                    ((OrganizationImpl) childOrganization).getAllChildOrganizations(organizations);
                }
            }
        }
        return organizations;
    }

    private void getAllChildOrganizations(Set<Organization> organizations) {
        if (getOrganizations().size() > 0) {
            Set<Organization> children = this.getOrganizations();
            for (Organization childOrganization : children) {
                organizations.add(childOrganization);
                if (!childOrganization.getOrganizations().isEmpty()) {
                    ((OrganizationImpl) childOrganization).getAllChildOrganizations(organizations);
                }
            }
        }
    }

    public void addShortcut(Xelement shortcut) {
        addReferenceToSet(SHORTCUTS, shortcut);
    }

    public void removeShortcut(Xelement shortcut) {
        removeReferenceFromSet(SHORTCUTS, shortcut);
    }

    public Set<Xelement> getShortcuts() {
        return getReferenceSet(SHORTCUTS, Xelement.class);
    }

    public Pricing getPricing() {
        Pricing pricing;
        if (this.getContentsByType(Pricing.class).iterator().hasNext()) {
            pricing = this.getContentsByType(Pricing.class).iterator().next();
            return pricing;
        } else {
            pricing = this.getPersistenceHelper().createElement(Pricing.class, this);
            return pricing;
        }
    }

    public void addExchangeElementContainer(XchangeElementContainer xchangeElementContainer) {
        contents.put(xchangeElementContainer.getId(), xchangeElementContainer);
    }

    public void removeExchangeElementContainer(XchangeElementContainer xchangeElementContainer) {
        contents.remove(xchangeElementContainer.getId());
    }

    public void moveExchangeElementContainerToHere(XchangeElementContainer xchangeElementContainer) {
        Xelement oldContainer = xchangeElementContainer.getContainedIn();
        if (oldContainer != null) {
            ((ContainerContainer) oldContainer).removeExchangeElementContainer(xchangeElementContainer);
        }
        this.addExchangeElementContainer(xchangeElementContainer);
        ((XelementImpl) xchangeElementContainer).setContainedIn(this);
        xchangeElementContainer.setLogicalContainer(this);
    }

    public String getLabel() {
        return ((getName() == null) ? "" : getName());
    }

    public String getName() {
        return getStringProperty(NAME);
    }

    public void setName(String name) {
        setStringProperty(NAME, name);
    }

    public void setDescription(String description) {
        setStringProperty(description, description);
    }

    public String getDescription() {
        return getStringProperty(DESCRIPTION);
    }

    @Override
    public Set<PropertyDefinition> getDefaultPropertyDefinitions() {
        return SchemaRepository.getInstance().getProperties(Organization.class);
    }

    public PartyAvailability getPartyAvailability() {
        return party.getPartyAvailability();
    }

    public int getAvailability(Day day) {
        return party.getAvailability(day);
    }

    public PartyAvailability setAvailability(Day from, Day to, Integer mondayMinutes, Integer tuesdayMinutes, Integer wednesdayMinutes, Integer thursdayMinutes, Integer fridayMinutes, Integer saturdayMinutes, Integer sundayMinutes, Party defaultAvailability) {
        return party.setAvailability(from, to, mondayMinutes, tuesdayMinutes, wednesdayMinutes, thursdayMinutes, fridayMinutes, saturdayMinutes, sundayMinutes, defaultAvailability);
    }

    public AvailabilityRecord getAvailabilityRecord(Day day) {
        return party.getAvailabilityRecord(day);
    }

    @Override
    public void delete() {
        for (Xelement xelement : getConsequentialDeletes()) {
            if (xelement instanceof Person) {
                ((Person) xelement).delete();
            }
        }
        super.delete();
    }

    @Override
    public Set<Xelement> getConsequentialDeletes() {
        Set<Xelement> setToBeDeleted = new HashSet<Xelement>();
        for (Xelement xelement : getConsequentialDeletesOnlyOrganizations()) {
            setToBeDeleted.add(xelement);
            for (Person person : ((Organization) xelement).getPersons()) {
                setToBeDeleted.add(person);
            }
        }
        setToBeDeleted.remove(this);
        return setToBeDeleted;
    }

    public Set<Xelement> getConsequentialDeletesOnlyOrganizations() {
        Set<Xelement> setToBeDeleted = new HashSet<Xelement>();
        setToBeDeleted.add(this);
        if (!this.getOrganizations().isEmpty()) {
            for (Organization orga : this.getOrganizations()) {
                setToBeDeleted.addAll(orga.getConsequentialDeletesOnlyOrganizations());
            }
        }
        return setToBeDeleted;
    }

    public void setAvailabilityOnSpecificDay(Day day, int minutes) {
        party.setAvailabilityOnSpecificDay(day, minutes);
    }

    public boolean isSubOrganizationOf(Organization organization) {
        boolean result = false;
        if (organization.getOrganizations().contains(this)) {
            result = true;
        }
        return result;
    }

    public boolean isTopOrganization() {
        boolean result = false;
        if (this.getContainedIn() instanceof Portfolio) {
            result = true;
        }
        return result;
    }

    public Set<XchangeElementContainer> getAllContainers() {
        return new HashSet<XchangeElementContainer>(getAllOrganizations());
    }

    @Override
    public boolean isValidContent(Xelement element) {
        if (element instanceof Person) {
            return true;
        }
        if (element instanceof Organization) {
            return true;
        }
        return false;
    }

    public Team createTeam(String name) {
        Team team = super.getPersistenceHelper().createElement(Team.class, this);
        team.setName(name);
        return team;
    }

    public Set<Team> getTeams() {
        return getContentsByType(Team.class);
    }
}
