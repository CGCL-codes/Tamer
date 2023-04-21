package net.sf.staccatocommons.leviathan;

import java.util.Map;
import ar.com.zauber.commons.dao.Closure;
import ar.com.zauber.leviathan.api.URIFetcherResponse;
import ar.com.zauber.leviathan.scrapper.spring.ScrapperClosureFactoryBean;

public class FetchingClosures {

    public static Closure<URIFetcherResponse> domainSwitching(Map<String, Closure<URIFetcherResponse>> domains) {
        try {
            return new ScrapperClosureFactoryBean(domains).getObject();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
