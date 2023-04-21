package model.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import distributions.Sampleble;
import model.project.Resource;

public class Definitions {

    public Map<String, Sampleble> idSamplebleMap = new HashMap<String, Sampleble>();

    public Map<String, List<Resource>> idResourcesMap = new HashMap<String, List<Resource>>();

    public Map<String, String> idDescriptionMap = new HashMap<String, String>();

    public Map<String, ArrayList<Double>> sourceDestinationProbabilityMap = new HashMap<String, ArrayList<Double>>();
}
