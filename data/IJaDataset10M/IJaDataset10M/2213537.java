package pl.edu.mimuw.loxim.protogen.api;

import java.io.File;
import pl.edu.mimuw.loxim.protogen.api.jaxb.EnumType;
import pl.edu.mimuw.loxim.protogen.api.jaxb.ProtocolType;
import pl.edu.mimuw.loxim.protogen.api.wrappers.EnumTypeWrapper;
import pl.edu.mimuw.loxim.protogen.api.wrappers.PackageGroupTypeWrapper;
import pl.edu.mimuw.loxim.protogen.api.wrappers.ProtocolTypeWrapper;

public class BasicLanguageGenerator implements LanguageGenerator {

    private PartialLanguageGenerator partialLanguageGenerator;

    public void generate(ProtocolType descriptor, File target_directory, File over) throws GeneratorException {
        target_directory.mkdirs();
        partialLanguageGenerator.setTarget_directory(target_directory);
        partialLanguageGenerator.setOverwrite_src_directory(over);
        ProtocolTypeWrapper protocol_wrapper = new ProtocolTypeWrapper(descriptor);
        partialLanguageGenerator.setDescriptor(protocol_wrapper);
        partialLanguageGenerator.init();
        partialLanguageGenerator.generateGlobals();
        for (EnumType enum_type : descriptor.getEnums().getEnum()) {
            partialLanguageGenerator.generateEnum(new EnumTypeWrapper(enum_type, partialLanguageGenerator.getDescriptor()));
        }
        for (PackageGroupTypeWrapper package_group_type : protocol_wrapper.getPackageGroupWrappers().values()) {
            partialLanguageGenerator.generatePackageGroup(package_group_type);
        }
        partialLanguageGenerator.generateTests();
    }

    public PartialLanguageGenerator getPartialLanguageGenerator() {
        return partialLanguageGenerator;
    }

    public void setPartialLanguageGenerator(PartialLanguageGenerator partialLanguageGenerator) {
        this.partialLanguageGenerator = partialLanguageGenerator;
    }
}
