package higraph.model.taggedInterfaces;

import higraph.model.interfaces.*;

public interface TaggedHigraph<T extends Tag<T, NP>, NP extends TaggedPayload<T, NP>, EP extends Payload<EP>, HG extends TaggedHigraph<T, NP, EP, HG, WG, SG, N, E>, WG extends TaggedWholeGraph<T, NP, EP, HG, WG, SG, N, E>, SG extends TaggedSubgraph<T, NP, EP, HG, WG, SG, N, E>, N extends TaggedNode<T, NP, EP, HG, WG, SG, N, E>, E extends TaggedEdge<T, NP, EP, HG, WG, SG, N, E>> extends Higraph<NP, EP, HG, WG, SG, N, E> {
}
