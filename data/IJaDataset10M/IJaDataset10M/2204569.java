package com.google.template.soy.tofu.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.template.soy.shared.internal.ModuleUtils;
import com.google.template.soy.shared.internal.SharedModule;
import com.google.template.soy.shared.restricted.SoyFunction;
import com.google.template.soy.shared.restricted.SoyPrintDirective;
import com.google.template.soy.sharedpasses.SharedPassesModule;
import com.google.template.soy.tofu.internal.BaseTofu.BaseTofuFactory;
import com.google.template.soy.tofu.restricted.SoyTofuFunction;
import com.google.template.soy.tofu.restricted.SoyTofuPrintDirective;
import java.util.Map;
import java.util.Set;

/**
 * Guice module for the Tofu backend.
 *
 * <p> Important: Do not use outside of Soy code (treat as superpackage-private).
 *
 */
public class TofuModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new SharedModule());
        install(new SharedPassesModule());
        bind(TofuEvalVisitorFactory.class);
        bind(TofuRenderVisitorFactory.class);
        install(new FactoryModuleBuilder().build(BaseTofuFactory.class));
    }

    /**
   * Builds and provides the map of SoyTofuFunctions (name to function).
   * @param soyFunctionsSet The installed set of SoyFunctions (from Guice Multibinder). Each
   *     SoyFunction may or may not implement SoyTofuFunction.
   */
    @Provides
    @Singleton
    Map<String, SoyTofuFunction> provideSoyTofuFunctionsMap(Set<SoyFunction> soyFunctionsSet) {
        return ModuleUtils.buildSpecificSoyFunctionsMap(SoyTofuFunction.class, soyFunctionsSet);
    }

    /**
   * Builds and provides the map of SoyTofuDirectives (name to directive).
   * @param soyDirectivesSet The installed set of SoyPrintDirectives (from Guice Multibinder). Each
   *     SoyDirective may or may not implement SoyTofuDirective.
   */
    @Provides
    @Singleton
    Map<String, SoyTofuPrintDirective> provideSoyTofuDirectivesMap(Set<SoyPrintDirective> soyDirectivesSet) {
        return ModuleUtils.buildSpecificSoyDirectivesMap(SoyTofuPrintDirective.class, soyDirectivesSet);
    }
}
