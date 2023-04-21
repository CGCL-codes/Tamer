package de.mogwai.kias.example.service.impl;

import de.mogwai.kias.example.bo.Freelancer;
import de.mogwai.kias.example.service.FreelancerService;
import de.mogwai.kias.example.service.PersistenceService;

public class FreelancerServiceImpl implements FreelancerService {

    private PersistenceService persistenceService;

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Freelancer findFreelancerByCode(String code) {
        Freelancer theFreelancer = (Freelancer) persistenceService.findByKey(Freelancer.class, "code", code);
        if (theFreelancer != null) return theFreelancer;
        return new Freelancer();
    }
}
