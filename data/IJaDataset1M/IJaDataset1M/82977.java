package com.google.gwt.user.rebind.rpc;

import java.io.PrintWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.core.ext.GeneratorContextExt;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Creates a client-side proxy for a {@link com.google.gwt.user.client.rpc.RemoteService RemoteService} interface as
 * well as the necessary type and field serializers.
 * 
 * Beispiel zur Nutzung:
 * Legende: 	__ wird ersetzt als -
 * 				_ wird ersetzt als .
 * 				X wird ersetzt als /
 * 
 * <pre>
 * <!-- START: Jsonp als RPC Kommunikation benutzen (Cross-Site Kommunikation)  -->
 * 	<generate-with
 * 		class="com.google.gwt.user.rebind.rpc.JsonpServiceInterfaceProxyGenerator">
 * 		<when-type-assignable class="com.google.gwt.user.client.rpc.RemoteService" />
 * 	</generate-with>
 * 	<!-- http://mo$swm$i_intra_swm_de/mvg/-->
 * 	<define-property name="gwt.rpc.proxySuperclass"
 * 		values="de_swm_gxt_client_mobile_network_JsonpServiceProxy" />
 * 	<set-property name="gwt.rpc.proxySuperclass"
 * 		value="de_swm_gxt_client_mobile_network_JsonpServiceProxy" />
 * 	<define-property name="gwt.rpc.jsonp.host"
 * 		values="mo__swm__i_intra_swm_deXmvgXmvgfahrplanX" />
 * 	<set-property name="gwt.rpc.jsonp.host"
 * 		value="mo__swm__i_intra_swm_deXmvgXmvgfahrplanX" />
 * 	<!-- ENDE Jsonp  -->
 * </pre>
 * 
 * @author wiese.daniel <br>
 *         copyright (C) 2011, SWM Services GmbH
 * 
 */
public class JsonpProxyCreator extends ProxyCreatorCp {

    private String jsonpHost;

    /**
	 * Default constructor.
	 * 
	 * @param serviceIntf
	 *            der constructor
	 */
    public JsonpProxyCreator(JClassType serviceIntf) {
        super(serviceIntf);
    }

    /**
	 * Creates the client-side proxy class.
	 * 
	 * @throws UnableToCompleteException
	 */
    public String create(TreeLogger logger, GeneratorContextExt context) throws UnableToCompleteException {
        try {
            String host = context.getPropertyOracle().getSelectionProperty(logger, "gwt.rpc.jsonp.host").getCurrentValue();
            if (host != null) {
                host = "http://" + host.replaceAll("__", "-");
                host = host.replaceAll("_", ".");
                host = host.replaceAll("X", "/");
                this.jsonpHost = host;
            }
        } catch (Exception e) {
        }
        return super.create(logger, context);
    }

    /**
	 * Generate the proxy constructor and delegate to the superclass constructor using the default address for the
	 * {@link com.google.gwt.user.client.rpc.RemoteService RemoteService}.
	 */
    @Override
    protected void generateProxyContructor(SourceWriter srcWriter) {
        srcWriter.println("public " + getProxySimpleName() + "() {");
        srcWriter.indent();
        srcWriter.println("super(\"" + this.jsonpHost + "\",");
        srcWriter.indent();
        srcWriter.println(getRemoteServiceRelativePath() + ", ");
        srcWriter.println("SERIALIZATION_POLICY, ");
        srcWriter.println("SERIALIZER);");
        srcWriter.outdent();
        srcWriter.outdent();
        srcWriter.println("}");
    }

    @Override
    public SourceWriter getSourceWriter(TreeLogger logger, GeneratorContextExt ctx, JClassType serviceAsync) {
        JPackage serviceIntfPkg = serviceAsync.getPackage();
        String packageName = serviceIntfPkg == null ? "" : serviceIntfPkg.getName();
        PrintWriter printWriter = ctx.tryCreate(logger, packageName, getProxySimpleName());
        if (printWriter == null) {
            return null;
        }
        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, getProxySimpleName());
        String[] imports = new String[] { getProxySupertype().getCanonicalName(), getStreamWriterClass().getCanonicalName(), SerializationStreamWriter.class.getCanonicalName(), GWT.class.getCanonicalName(), ResponseReader.class.getCanonicalName(), SerializationException.class.getCanonicalName(), RpcToken.class.getCanonicalName(), RpcTokenException.class.getCanonicalName(), Impl.class.getCanonicalName(), RpcStatsContext.class.getCanonicalName() };
        for (String imp : imports) {
            composerFactory.addImport(imp);
        }
        String rpcSuper = getProxySupertype().getSimpleName();
        try {
            rpcSuper = ctx.getPropertyOracle().getSelectionProperty(logger, "gwt.rpc.proxySuperclass").getCurrentValue();
            if (rpcSuper != null) {
                rpcSuper = rpcSuper.replaceAll("_", ".");
            }
        } catch (Exception e) {
        }
        composerFactory.setSuperclass(rpcSuper);
        composerFactory.addImplementedInterface(serviceAsync.getErasedType().getQualifiedSourceName());
        return composerFactory.createSourceWriter(ctx, printWriter);
    }
}
