package sk.ygor.raynor.client.service;

import sk.ygor.raynor.client.json.JSONSerializer;

public interface JSONServiceFactory {

    public static interface Configuration {

        @SuppressWarnings({ "UnusedDeclaration" })
        public void setServiceInvoker(JSONService.Invoker serviceInvoker);

        @SuppressWarnings({ "UnusedDeclaration" })
        public void setServiceTransformer(JSONService.Transformer serviceTransformer);

        @SuppressWarnings({ "UnusedDeclaration" })
        public void setSerializerTransformer(JSONSerializer.Transformer serializerTransformer);
    }
}
